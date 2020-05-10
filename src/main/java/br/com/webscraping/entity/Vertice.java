package br.com.webscraping.entity;

public class Vertice {
	
	private String _codigo;	
	private String _longitude;
	private String _longSigma;
	private String _latitude;
	private String _latSigma;
	private String _altitude;
	private String _altSigma;
	private String _metodPosicionamento;
	private String _cns;
	
	public String get_cns() {
		return _cns;
	}
	public void set_cns(String cns) {
		this._cns = cns;
	}
	
	public String get_codigo() {
		return _codigo;
	}
	public void set_codigo(String _codigo) {
		this._codigo = _codigo;
	}
	public String get_longitude() {
		return _longitude;
	}
	public void set_longitude(String _longitude) {
		this._longitude = _longitude;
	}
	public String get_longSigma() {
		return _longSigma;
	}
	public void set_longSigma(String _longSigma) {
		this._longSigma = _longSigma;
	}
	public String get_latitude() {
		return _latitude;
	}
	public void set_latitude(String _latitude) {
		this._latitude = _latitude;
	}
	public String get_latSigma() {
		return _latSigma;
	}
	public void set_latSigma(String _latSigma) {
		this._latSigma = _latSigma;
	}
	public String get_altitude() {
		return _altitude;
	}
	public void set_altitude(String _altitude) {
		this._altitude = _altitude;
	}
	public String get_altSigma() {
		return _altSigma;
	}
	public void set_altSigma(String _altSigma) {
		this._altSigma = _altSigma;
	}
	public String get_metodPosicionamento() {
		return _metodPosicionamento;
	}
	public void set_metodPosicionamento(String _metodPosicionamento) {
		this._metodPosicionamento = _metodPosicionamento;
	}
}
