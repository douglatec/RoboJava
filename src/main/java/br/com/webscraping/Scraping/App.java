package br.com.webscraping.Scraping;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import br.com.webscraping.entity.Limite;
import br.com.webscraping.entity.Vertice;
import br.com.webscraping.jsoup.Connect;
import br.com.webscraping.report.CriarExcel;
import br.com.webscraping.util.Util;

public class App {

	public static void main(String[] args) throws Exception {

		var paginaAtual = 1;
		Boolean ultimaPaginaEncontrada = false;
		int ultimaPagina = 0;

		List<Vertice> vertices = new ArrayList<>();
		List<Limite> limites = new ArrayList<>();

		String cns = "09.994-5";// 06.241-4

		do {

			System.out.println("Pagina atual: " + paginaAtual);

			String url = "https://sigef.incra.gov.br/consultar/parcelas/?page=%s&termo=&pesquisa_avancada=True&cpf_cnpj=&proprietario=&cns=%s&matricula=&codigo=&protocolo=&credenciado=&vertice=&sncr=";
			String urlConsulta = String.format(url, paginaAtual, cns);

			Connect.enableSSLSocket();
			Document document = Connect.jsoupConnect(urlConsulta);

			if (ultimaPaginaEncontrada == false) {

				// localizar a numero da ultima pagina
				Element paginacao = document.getElementsByClass("pagination").first();

				Elements lis = paginacao.selectFirst("ul").getElementsByTag("li");
				int numeroDePaginas = lis.size();

				var ultimaPaginaLi = lis.get(numeroDePaginas - 2);
				var nr = ultimaPaginaLi.selectFirst("a").text();

				ultimaPagina = Integer.parseInt(nr);
				ultimaPaginaEncontrada = true;

				System.out.println("Numero de paginas: " + ultimaPagina);
			}

			Element tabelaParcelas = document.getElementById("tbl-parcelas tbody");
			Elements linhas = tabelaParcelas.getElementsByTag("tr");

			for (int indiceLinha = 0; indiceLinha < linhas.size(); indiceLinha++) {
				Element linha = linhas.get(indiceLinha);
				Elements colunas = linha.getElementsByTag("td");
				Element colunaNome = colunas.get(0);
				String href = colunaNome.select("a").attr("href");
				String[] textSplit = href.split("/");
				String id = textSplit[4];
				Document detalhesDoc = null;

				try {

					System.out.println("Parcela id " + id);
					String urlParcela = "https://sigef.incra.gov.br/geo/parcela/detalhe/" + id + "/";
					Connect.enableSSLSocket();
					detalhesDoc = Connect.jsoupConnect(urlParcela);

				} catch (Exception e) {
					var xx = e.getMessage();
					System.out.println(xx);
				}

				Element verticesTable = Util.encontreTabelaPorNome(detalhesDoc, "Vértices da parcela");
				Elements linhasVertices = verticesTable.getElementsByTag("tr");

				int totalVertices = linhasVertices.size();
				System.out.println("Total de vertices " + totalVertices);
				// indiceLinhaVertices = 1 = because the first row is the col names so skip it.
				for (int indiceLinhaVertices = 1; indiceLinhaVertices < totalVertices; indiceLinhaVertices++) {
					Element linhaVertices = linhasVertices.get(indiceLinhaVertices);
					Elements colunasVertices = linhaVertices.getElementsByTag("td");
					Vertice vertice = new Vertice();

					vertice.set_cns(cns);
					vertice.set_codigo(colunasVertices.get(0).text());
					vertice.set_longitude(colunasVertices.get(1).text());
					vertice.set_longSigma(colunasVertices.get(2).text());
					vertice.set_latitude(colunasVertices.get(3).text());
					vertice.set_latSigma(colunasVertices.get(4).text());
					vertice.set_altitude(colunasVertices.get(5).text());
					vertice.set_altSigma(colunasVertices.get(6).text());
					vertice.set_metodPosicionamento(colunasVertices.get(7).text());

					vertices.add(vertice);
				}

				Element limitesTable = Util.encontreTabelaPorNome(detalhesDoc, "Limites");
				Elements linhaslimites = limitesTable.getElementsByTag("tr");

				int totalLimites = linhaslimites.size();
				System.out.println("Total de limites " + totalLimites);
				// indiceLinhaLimites = 1 = because the first row is the col names so skip it.
				for (int indiceLinhaLimites = 1; indiceLinhaLimites < totalLimites; indiceLinhaLimites++) {
					Element linhaVertices = linhaslimites.get(indiceLinhaLimites);
					Elements colunasLimites = linhaVertices.select("td");
					Limite limite = new Limite();

					limite.set_cns(cns);
					limite.set_doVertice(colunasLimites.get(0).text());
					limite.set_aoVertice(colunasLimites.get(1).text());
					limite.set_tipo(colunasLimites.get(2).text());
					limite.set_lado(colunasLimites.get(3).text());
					limite.set_azimute(colunasLimites.get(4).text());
					limite.set_comprimento(colunasLimites.get(5).text());
					limite.set_confrontante(colunasLimites.get(6).text());

					limites.add(limite);
				}
			}

			paginaAtual++;
		} while (paginaAtual <= ultimaPagina);

		CriarExcel criarExcel = new CriarExcel();

		System.out.println("Gerando excel.");
		criarExcel.gerar(vertices, limites);

	}

}