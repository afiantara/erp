package sf.purchasing;

import sf.general.General;

public class POHeader extends General{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String nobukti;
	private String tglBukti;
	private String kvendor;
	private String kcompany;
	private String kvaluta;
	private String adappn="T";
	private double jtempo=0;
	private double blain_nilai=0;
	private String blain_ket;
	private String keterangan;
	private String approvedBy;
	private String approvedTgl;
	private String recStatus="A";
	private String userInput;
	private String tglInput;
	private String tglUpdate;
	private String userUpdate;
	private String delivery;
	private POHeader[] headers;
	
	public void setNobukti(String nobukti) {
		this.nobukti = nobukti;
	}
	public String getNobukti() {
		return nobukti;
	}
	public void setTglBukti(String tglBukti) {
		this.tglBukti = tglBukti;
	}
	public String getTglBukti() {
		return tglBukti;
	}
	public void setKvendor(String kvendor) {
		this.kvendor = kvendor;
	}
	public String getKvendor() {
		return kvendor;
	}
	
	private String _err="";
	public String getErr()
	{
		return _err;
	}
	
	public boolean checkIsNULL()
	{
		_err="";
		
		if(this.getNobukti()==null || "".equals(this.getNobukti()))
		{
			_err="Error, Invalid No Bukti Could not empty.";
			return true;
		}
		if(this.getTglBukti()==null || "".equals(this.getTglBukti()))
		{
			_err="Error, Tanggal Bukti Could not empty.";
			return true;
		}
		if(this.getKvendor()==null || "".equals(this.getKvendor()))
		{
			_err="Error, Invalid Kode Vendor Could not empty.";
			return true;
		}
		if(this.getKcompany()==null || "".equals(this.getKcompany()))
		{
			_err="Error, Invalid Kode Company Could not empty.";
			return true;
		}
		if(this.getKvaluta()==null || "".equals(this.getKvaluta()))
		{
			_err="Error, Invalid Kode Valuta Could not empty.";
			return true;
		}
		if(this.getApprovedtgl()==null || "".equals(this.getApprovedtgl()))
		{
			_err="Error, Invalid Tanggal Approved Could not empty.";
			return true;
		}
		if(this.getTglInput()==null || "".equals(this.getTglInput()))
		{
			_err="Error, Invalid Tanggal Input Could not empty.";
			return true;
		}if(this.getTglUpdate()==null || "".equals(this.getTglUpdate()))
		{
			_err="Error, Invalid Tanggal Update Could not empty.";
			return true;
		}
		return false;
	}
	
	/**
	 * @param headers the headers to set
	 */
	public void setHeaders(POHeader[] headers) {
		this.headers = headers;
	}
	/**
	 * @return the headers
	 */
	public POHeader[] getHeaders() {
		return headers;
	}
	/**
	 * @param kcompany the kcompany to set
	 */
	public void setKcompany(String kcompany) {
		this.kcompany = kcompany;
	}
	/**
	 * @return the kcompany
	 */
	public String getKcompany() {
		return kcompany;
	}
	/**
	 * @param jtempo the jtempo to set
	 */
	public void setJtempo(double jtempo) {
		this.jtempo = jtempo;
	}
	/**
	 * @return the jtempo
	 */
	public double getJtempo() {
		return jtempo;
	}
	/**
	 * @param keterangan the keterangan to set
	 */
	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}
	/**
	 * @return the keterangan
	 */
	public String getKeterangan() {
		return keterangan;
	}
	/**
	 * @param approvedby the approvedby to set
	 */
	public void setApprovedby(String approvedby) {
		this.approvedBy = approvedby;
	}
	/**
	 * @return the approvedby
	 */
	public String getApprovedby() {
		return approvedBy;
	}
	/**
	 * @param approvedtgl the approvedtgl to set
	 */
	public void setApprovedtgl(String approvedtgl) {
		this.approvedTgl = approvedtgl;
	}
	/**
	 * @return the approvedtgl
	 */
	public String getApprovedtgl() {
		return approvedTgl;
	}
	
	/**
	 * @param kvaluta the kvaluta to set
	 */
	public void setKvaluta(String kvaluta) {
		this.kvaluta = kvaluta;
	}
	/**
	 * @return the kvaluta
	 */
	public String getKvaluta() {
		return kvaluta;
	}
	/**
	 * @param adappn the adappn to set
	 */
	public void setAdappn(String adappn) {
		this.adappn = adappn;
	}
	/**
	 * @return the adappn
	 */
	public String getAdappn() {
		return adappn;
	}
	/**
	 * @param blain_nilai the blain_nilai to set
	 */
	public void setBlain_nilai(double blain_nilai) {
		this.blain_nilai = blain_nilai;
	}
	/**
	 * @return the blain_nilai
	 */
	public double getBlain_nilai() {
		return blain_nilai;
	}
	/**
	 * @param blain_ket the blain_ket to set
	 */
	public void setBlain_ket(String blain_ket) {
		this.blain_ket = blain_ket;
	}
	/**
	 * @return the blain_ket
	 */
	public String getBlain_ket() {
		return blain_ket;
	}
	/**
	 * @param recStatus the recStatus to set
	 */
	public void setRecStatus(String recStatus)
	{
		this.recStatus = recStatus;		
	}
	/**
	 * @return the recStatus
	 */
	public String getRecStatus()
	{
		return recStatus;
	}
	/**
	 * @param userInput the userInput to set
	 */
	public void setUserInput(String userInput)
	{
		this.userInput = userInput;
	}
	/**
	 * @return the userInput
	 */
	public String getUserInput()
	{
		return userInput;
	}
	/**
	 * @param tglInput the tglInput to set
	 */
	public void setTglInput(String tglInput)
	{
		this.tglInput = tglInput;	
	}
	/**
	 * @return the tglInput
	 */
	public String getTglInput()
	{
		return tglInput;
	}
	/**
	 * @param tglUpdate the tglUpdate to set
	 */
	public void setTglUpdate(String tglUpdate)
	{
		this.tglUpdate = tglUpdate;
	}
	/**
	 * @return the tglUpdate
	 */
	public String getTglUpdate()
	{
		return tglUpdate;
	}
	/**
	 * @param userUpdate the userUpdate to set
	 */
	public void setUserUpdate(String userUpdate)
	{
		this.userUpdate = userUpdate;
	}
	/**
	 * @return the userUpdate
	 */
	public String getUserUpdate()
	{
		return userUpdate;
	}
	/**
	 * @param nodelivery the nodelivery to set
	 */
	public void setDelivery(String delivery) {
		this.delivery = delivery;
	}
	/**
	 * @return the nodelivery
	 */
	public String getDelivery() {
		return delivery;
	}	
}
