package xixi.transport.netty.pipeline;

import static org.jboss.netty.channel.Channels.pipeline;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.frame.FrameDecoder;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;
import org.jboss.netty.handler.traffic.GlobalTrafficShapingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TcpServerPipelineFactory implements ChannelPipelineFactory{
    private static Logger logger = LoggerFactory.getLogger(TcpServerPipelineFactory.class);

    private GlobalTrafficShapingHandler globalTrafficShapingHandler;
    private SimpleChannelUpstreamHandler tcpRequestHandler;
    private FrameDecoder tcpRequestDecoder;
    private OneToOneEncoder tcpResponseEncoder;

    public ChannelPipeline getPipeline() throws Exception {
        logger.debug("create new pipeline");
        ChannelPipeline pipeline = pipeline();
        //注意Codec的线程安全
        pipeline.addLast("tcpRequestDecoder", tcpRequestDecoder);
      //  pipeline.addLast("GLOBAL_TRAFFIC_SHAPING", globalTrafficShapingHandler);
        pipeline.addLast("handler", tcpRequestHandler);
        pipeline.addLast("tcpResponseEncoder", tcpResponseEncoder);
        return pipeline;
    }

    public void setGlobalTrafficShapingHandler(GlobalTrafficShapingHandler globalTrafficShapingHandler) {
        this.globalTrafficShapingHandler = globalTrafficShapingHandler;
    }

    public void setTcpRequestHandler(SimpleChannelUpstreamHandler tcpRequestHandler) {
        this.tcpRequestHandler = tcpRequestHandler;
    }

    public void setTcpRequestDecoder(FrameDecoder tcpRequestDecoder) {
		this.tcpRequestDecoder = tcpRequestDecoder;
	}

	public void setTcpResponseEncoder(OneToOneEncoder tcpResponseEncoder) {
        this.tcpResponseEncoder = tcpResponseEncoder;
    }
}
