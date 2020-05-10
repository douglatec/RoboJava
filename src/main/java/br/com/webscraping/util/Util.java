package br.com.webscraping.util;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Util {

	public static Element encontreTabelaPorNome(Document document, String cabecaho) {
		var divParcelasCabecalho = document.selectFirst("div:containsOwn(" + cabecaho + ")");
		var divParcelasConteudo = divParcelasCabecalho.nextElementSibling();
		return divParcelasConteudo.selectFirst("table");
	}

}
