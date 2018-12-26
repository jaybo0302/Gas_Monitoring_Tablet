package com.peric.gas_monitoring_tablet.utils;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class MyCodecFactory4Net implements ProtocolCodecFactory {
	@Override
	public ProtocolDecoder getDecoder(IoSession arg0) throws Exception {
		return new MyDecoder4Net();
	}
	@Override
	public ProtocolEncoder getEncoder(IoSession arg0) throws Exception {
		return new MyEncoder();
	}
}
