package br.com.webscraping.Scraping;

import java.io.FileInputStream;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class App {
	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36(KHTML, like Gecko) Chrome/80.0.3987.163 Safari/537.36";

	public static void main(String[] args) throws Exception {
		/*
		 * GET START
		 * 
		 * Document document = Jsoup.connect("https://www.imdb.com/chart/top").get();
		 * 
		 * for (Element row : document.select("table.chart.full-width tr")) {
		 * 
		 * String title = row.select(".titleColumn a").text(); // String title =
		 * row.select(".titleColumn").text(); String rating =
		 * row.select(".imdbRating").text();
		 * 
		 * System.out.println(title + "->Rating: " + rating); }
		 */

		// SIGEF

		var paginaAtual = 1;
		Boolean ultimaPaginaEncontrada = false;
		int ultimaPagina = 0;

		List<Vertice> vertices = new ArrayList<>();
		List<Limite> limites = new ArrayList<>();

		String cns = "09.994-5";// 06.241-4

		do {

			System.out.println("Pagina atual: " + paginaAtual);

			String url = "https://sigef.incra.gov.br/consultar/parcelas/?page=%s&termo=&pesquisa_avancada=True&cpf_cnpj=&proprietario=&cns=%s&matricula=&codigo=&protocolo=&credenciado=&vertice=&sncr=";
			String urlFormatada = String.format(url, paginaAtual, cns);

			enableSSLSocket();
			Document document = Jsoup.connect(urlFormatada).timeout(0).userAgent(USER_AGENT).data("name", "jsoup")
					.get();

		
			if (ultimaPaginaEncontrada == false) {

				//localizar a numero da ultima pagina
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
					enableSSLSocket();
					detalhesDoc = Jsoup.connect("https://sigef.incra.gov.br/geo/parcela/detalhe/" + id + "/").timeout(0)
							.userAgent(USER_AGENT).data("name", "jsoup").get();

				} catch (Exception e) {
					var xx = e.getMessage();
					System.out.println(xx);
				}
			

				Element verticesTable = encontreTabelaPorNome(detalhesDoc, "Vértices da parcela");
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

				Element limitesTable = encontreTabelaPorNome(detalhesDoc, "Limites");
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

	private static Element encontreTabelaPorNome(Document document, String cabecaho) {
		var divParcelasCabecalho = document.selectFirst("div:containsOwn(" + cabecaho +")");
		var divParcelasConteudo = divParcelasCabecalho.nextElementSibling();
		return divParcelasConteudo.selectFirst("table");
	}

	// https://nanashi07.blogspot.com/2014/06/enable-ssl-connection-for-jsoup.html
	public static void enableSSLSocket() throws KeyManagementException, NoSuchAlgorithmException {
		HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		});

		SSLContext context = SSLContext.getInstance("TLS");
		context.init(null, new X509TrustManager[] { new X509TrustManager() {
			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}

			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}

			public X509Certificate[] getAcceptedIssuers() {
				return new X509Certificate[0];
			}
		} }, new SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
	}
}