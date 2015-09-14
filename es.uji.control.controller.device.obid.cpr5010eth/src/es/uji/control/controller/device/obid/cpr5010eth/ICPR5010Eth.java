package es.uji.control.controller.device.obid.cpr5010eth;


public interface ICPR5010Eth {
	public CPR5010EthInstance open(String address);
	public CPR5010EthInstance open(String address, int port);
	public void close(CPR5010EthInstance instance);
}
