package br.com.webscraping.Scraping;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

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

		// Get Start SIGEF

		/*
		 * Document document = Jsoup.connect(
		 * "https://sigef.incra.gov.br/consultar/parcelas/?termo=&pesquisa_avancada=True&cpf_cnpj=&proprietario=&cns=09.994-5&matricula=&codigo=&protocolo=&credenciado=&vertice=&sncr=")
		 * .userAgent("Chrome").data("name", "jsoup").get();
		 */
		
		String url = "https://sigef.incra.gov.br/consultar/parcelas/?termo=&pesquisa_avancada=True&cpf_cnpj=&proprietario=&cns=09.994-5&matricula=&codigo=&protocolo=&credenciado=&vertice=&sncr=";
		
		enableSSLSocket();
		Document document = Jsoup.connect(url)								
				.userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.163 Safari/537.36")
				.data("name", "jsoup")
				.get();
		
		/*
		Element test = document.getElementById("tbl-parcelas tbody"); ---> Verificar a possibilidade de utilizar o arquivo cvs gerado por um link acessado atraves dessalinha de condigo
		System.out.println(test);
		
		*/
		Element test = document.getElementById("tbl-parcelas tbody").getElementsByTag("td").first().getElementsByTag("a").first();
		
		//Element test = document.getElementById("tbl-parcelas tbody").getElementsByTag("td").first();	
		
	
		//System.out.println(test.getElementsByClass("href="));
		
		//System.out.println(test.getElementsByAttribute("href").toString());
		
		String textoSeparado = test.toString();
		
		String[] textSplit = textoSeparado.split("/");
		
		
		//System.out.println(Arrays.toString(textSplit));
		
		System.out.println(textSplit[4]);
		
		
		enableSSLSocket();
		Document document2 = Jsoup.connect("https://sigef.incra.gov.br/geo/parcela/detalhe/"+ textSplit[4] +"/")								
				.userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.163 Safari/537.36")
				.data("name", "jsoup")
				.get();
		
		System.out.println(document2);
		
		
		
		/*NAO CONSIGO AVANÇAR DAQUI TEM ALGUMA IDEIA DE COMO FAÇO PARA CAPTURAR OS VERTICES E LIMITES
		 
		Element test2 = document.getElementById("tbody").getElementsByTag("tr").first().getElementsByTag("td").first();
		System.out.println(test2);
			*/	
	}
	
	//https://nanashi07.blogspot.com/2014/06/enable-ssl-connection-for-jsoup.html
	public static void enableSSLSocket() throws KeyManagementException, NoSuchAlgorithmException {
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
 
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, new X509TrustManager[]{new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }
 
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }
 
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        }}, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
    }
}