package NettyHttp.NettyHttp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;
import java.net.InetAddress;

/**
 * 
* Title: NettyServerHandler
* Description: 服务端业务逻辑
* Version:1.0.0  
* @author zk
* @date 2018-4-17
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
	//private String result="";
	/*
	 * 收到消息时，返回信息
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ResultInfo rsp = new ResultInfo();
		int machineId=rsp.getMachineId();
		int tradeid=rsp.getTradeid();
		double price=rsp.getPrice();
		String result = rsp.getResult();
		String [] bodys ;
		if(! (msg instanceof FullHttpRequest)){
			
			//send(ctx,result,HttpResponseStatus.BAD_REQUEST);
			return;
	 	}
		FullHttpRequest httpRequest = (FullHttpRequest)msg;
		try{
			String path=httpRequest.uri();			//获取路径
			String body = getBody(httpRequest); 	//获取参数
			HttpMethod method=httpRequest.method();//获取请求方法
			//如果不是这个路径，就直接返回错误
			if(!path.contains("/pay")){
				//result="非法请求!";
				//send(ctx,result,HttpResponseStatus.BAD_REQUEST);
				return;
			}
			//如果是GET请求
			if(HttpMethod.GET.equals(method)){ 
				//接受到的消息，做业务逻辑处理...
				System.out.println("body:"+path.substring(5));
				body =path.substring(5);
				bodys =body.split("/");
				for(String bString:bodys) {
					int size = bString.indexOf("=");
					try {
						String  xString = bString.substring(size+1);
						if("machineId".equals(bString.substring(0, size))) {
						
							machineId = Integer.parseInt(xString);
						}else if("price".equals(bString.substring(0, size))) {
						
							price = Double.parseDouble(xString);
						}else if("tradeid".equals(bString.substring(0, size))){
						
							tradeid = Integer.parseInt(xString);
						}else {
							result = "false";
						}
						}catch (Exception e) {
							System.out.println("http---get----Exception:"+e);
						}
				}
				System.out.println("http---------machineId:"+machineId);
				System.out.println("http---------price:"+price);
				System.out.println("http---------tradeid:"+tradeid);
				result="true";
				path =path+"/plain";
				System.out.println(path);
				send(ctx,result,HttpResponseStatus.OK,path);
				return;
			}
			//如果是POST请求
			if(HttpMethod.POST.equals(method)){ 
				//接受到的消息，做业务逻辑处理...
				System.out.println("body:"+body);
				String []values = body.split("\\r\\n");
				for(int x =0;x<values.length;x++) {
					try {
						if(values[x].contains("machineId")) {
							machineId = Integer.parseInt(values[x+2]);
							
						}else if (values[x].contains("price")) {
							price = Double.parseDouble(values[x+2]);
							
						}else if (values[x].contains("tradeid")) {
							tradeid = Integer.parseInt(values[x+2]);
							
						}else {
							
						}
					}catch (Exception e) {
						System.out.println("http---get----Exception:"+e);
					}
				}
				System.out.println("http---------machineId:"+machineId);
				System.out.println("http---------price:"+price);
				System.out.println("http---------tradeid:"+tradeid);
				result="true";
				path =path+"/plain";
				send(ctx,result,HttpResponseStatus.OK,path);
				return;
			}
		}catch(Exception e){
			System.out.println("处理请求失败!");
			e.printStackTrace();
		}finally{
			//释放请求
			httpRequest.release();
		}
	}
   
	/**
	 * 获取body参数
	 * @param request
	 * @return
	 */
	private String getBody(FullHttpRequest request){
		ByteBuf buf = request.content();
		return buf.toString(CharsetUtil.UTF_8);
	}
	
	/**
	 * 发送的返回值
	 * @param ctx	  返回
	 * @param context 消息
	 * @param status 状态
	 */
	private void send(ChannelHandlerContext ctx, String context,HttpResponseStatus status,String path) {
		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, Unpooled.copiedBuffer(context, CharsetUtil.UTF_8));
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, path);
		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	}
	
	/*
	 * 建立连接时，返回消息
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("连接的客户端地址:" + ctx.channel().remoteAddress());
		ctx.writeAndFlush("客户端"+ InetAddress.getLocalHost().getHostName() + "成功与服务端建立连接！ ");
		super.channelActive(ctx);
	}
}