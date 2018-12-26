package com.peric.gas_monitoring_tablet.utils;

import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

public class DataReceiveServer {
	private static IoAcceptor acceptor = null; // 创建连接;
	private static InetSocketAddress isa = new InetSocketAddress(3241);
	public static void start() {
		try {
			// 创建一个非阻塞的server端的Socket 可以不指定线程数量，MINA2里面默认是CPU数量+2 
			acceptor = new NioSocketAcceptor();
			((NioSocketAcceptor)acceptor).setReuseAddress(true);
			// 设置过滤器（使用Mina提供的文本换行符编解码器）
			Executor threadPool = Executors.newFixedThreadPool(100);
			//加入过滤器（Filter）到Acceptor
			acceptor.getFilterChain().addLast("exector", new ExecutorFilter(threadPool));
			acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new MyCodecFactory4Net()));
			// 绑定逻辑处理器
			acceptor.setHandler(new MyServerHandler4Net()); // 添加业务处理
			// 绑定逻辑处理器
			acceptor.setHandler(new MyServerHandler4Net()); // 添加业务处理
			// 设置读取数据的缓冲区大小
			acceptor.getSessionConfig().setReadBufferSize(2048);
			// 读写通道10秒内无操作进入空闲状态
			acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
			// 绑定端口
			acceptor.bind(isa);
			System.out.println("服务端启动成功...     端口号为：" + 3241);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void stop() {
		if (acceptor != null) {
			acceptor.dispose();
		}
	}
}