package xixi.transport.netty.pipeline;

import static org.jboss.netty.channel.Channels.pipeline;

import java.util.List;

import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.handler.codec.frame.FrameDecoder;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;
import org.jboss.netty.handler.traffic.GlobalTrafficShapingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TcpClientPipelineFactory implements ChannelPipelineFactory {
    private static Logger logger = LoggerFactory.getLogger(TcpClientPipelineFactory.class);

    private GlobalTrafficShapingHandler globalTrafficShapingHandler;
    private FrameDecoder tcpResponseDecoder;
    private OneToOneEncoder tcpRequestEncoder;
    private List<ChannelHandler> otherHandlers;

    public ChannelPipeline getPipeline() throws Exception {
        logger.debug("create new pipeline");
        ChannelPipeline pipeline = pipeline();
        //注意Codec的线程安全
        pipeline.addLast("tcpResponseDecoder", tcpResponseDecoder);
       // pipeline.addLast("GLOBAL_TRAFFIC_SHAPING", globalTrafficShapingHandler);
        pipeline.addLast("tcpRequestEncoder", tcpRequestEncoder);
        for(ChannelHandler handler : otherHandlers){
            pipeline.addLast(handler.toString(),handler);
        }
        return pipeline;
    }

    public void setGlobalTrafficShapingHandler(GlobalTrafficShapingHandler globalTrafficShapingHandler) {
        this.globalTrafficShapingHandler = globalTrafficShapingHandler;
    }

    public void setTcpRequestEncoder(OneToOneEncoder tcpRequestEncoder) {
        this.tcpRequestEncoder = tcpRequestEncoder;
    }


    public List<ChannelHandler> getOtherHandlers() {
        return otherHandlers;
    }

    public void setOtherHandlers(List<ChannelHandler> otherHandlers) {
        this.otherHandlers = otherHandlers;
    }

	public FrameDecoder getTcpResponseDecoder() {
		return tcpResponseDecoder;
	}

	public void setTcpResponseDecoder(FrameDecoder tcpResponseDecoder) {
		this.tcpResponseDecoder = tcpResponseDecoder;
	}
}
