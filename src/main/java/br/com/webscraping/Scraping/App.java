package br.com.webscraping.Scraping;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Hello world!
 *
 */
public class App {
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

		Document document = Jsoup.connect("https://sigef.incra.gov.br/consultar/parcelas/").get();

		// System.out.println(document.outerHtml());

		// https://sigef.incra.gov.br/consultar/parcelas/?termo=&pesquisa_avancada=True&cpf_cnpj=&proprietario=&cns=09.994-5&matricula=&codigo=&protocolo=&credenciado=&vertice=&sncr=

	}
}
