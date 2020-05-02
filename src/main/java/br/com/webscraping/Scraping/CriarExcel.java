package br.com.webscraping.Scraping;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class CriarExcel {

	private static final String fileName = "C:\\Users\\dougl\\Documents\\Sigef\\novo.xls";

	public void gerar(List<Vertice> vertices, List<Limite> limites) {
		HSSFWorkbook workbook = new HSSFWorkbook();

		gerarVertices(vertices, workbook);
		gerarLimites(limites, workbook);

		try {
			FileOutputStream out = new FileOutputStream(new File(CriarExcel.fileName));
			workbook.write(out);
			out.close();
			System.out.println("Arquivo Excel criado com sucesso!");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Arquivo não encontrado!");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Erro na edição do arquivo!");
		}

	}

	private void gerarVertices(List<Vertice> vertices, HSSFWorkbook workbook) {
		HSSFSheet sheetVertices = workbook.createSheet("Vertices");

		var colunas = "Código,Longitude,Sigma Long. (m),Latitude,Sigma Lat. (m),Altitude (m),Sigma Altitude (m),Método Posicionamento"
				.split(",");

		Row linhaCabecalho = sheetVertices.createRow(0);

		for (int indiceColuna = 0; indiceColuna < colunas.length; indiceColuna++) {

			Cell cell = linhaCabecalho.createCell(indiceColuna);
			cell.setCellValue(colunas[indiceColuna]);
		}

		int rownum = 1;
		for (Vertice vertice : vertices) {
			Row row = sheetVertices.createRow(rownum++);

			Cell cellLatitude = row.createCell(1);
			cellLatitude.setCellValue(vertice.get_latitude());

			Cell cellLatiSigma = row.createCell(2);
			cellLatiSigma.setCellValue(vertice.get_latSigma());

			Cell cellLongitude = row.createCell(3);
			cellLongitude.setCellValue(vertice.get_longitude());

			Cell cellLongSigma = row.createCell(4);
			cellLongSigma.setCellValue(vertice.get_longSigma());

			Cell cellAltitude = row.createCell(5);
			cellAltitude.setCellValue(vertice.get_altitude());

			Cell cellAltitudeSigma = row.createCell(6);
			cellAltitudeSigma.setCellValue(vertice.get_altSigma());

			Cell cellMetodoPosicionamento = row.createCell(7);
			cellMetodoPosicionamento.setCellValue(vertice.get_metodPosicionamento());

			Cell cellCodigo = row.createCell(0);
			cellCodigo.setCellValue(vertice.get_codigo());

		}
	}

	private void gerarLimites(List<Limite> limites, HSSFWorkbook workbook) {
		HSSFSheet sheetLmites = workbook.createSheet("Limites");

		var colunas = "Do Vértice,Ao Vértice,Tipo,Lado,Azimute,Comprimento (m),Confrontante".split(",");

		Row linhaCabecalho = sheetLmites.createRow(0);

		for (int indiceColuna = 0; indiceColuna < colunas.length; indiceColuna++) {

			Cell cell = linhaCabecalho.createCell(indiceColuna);
			cell.setCellValue(colunas[indiceColuna]);
		}

		int rownum = 1;
		for (Limite limite : limites) {
			Row row = sheetLmites.createRow(rownum++);

			Cell cellDoVertice = row.createCell(0);
			cellDoVertice.setCellValue(limite.get_doVertice());

			Cell cellAoVertice = row.createCell(1);
			cellAoVertice.setCellValue(limite.get_aoVertice());

			Cell cellTipo = row.createCell(2);
			cellTipo.setCellValue(limite.get_tipo());

			Cell cellLado = row.createCell(3);
			cellLado.setCellValue(limite.get_lado());

			Cell cellAzimute = row.createCell(4);
			cellAzimute.setCellValue(limite.get_azimute());

			Cell cellComprimento = row.createCell(5);
			cellComprimento.setCellValue(limite.get_comprimento());

			Cell cellConfrontante = row.createCell(6);
			cellConfrontante.setCellValue(limite.get_confrontante());

		}

	}

}
