import java.io.*;   
import java.net.*;   
import java.text.SimpleDateFormat;
import java.util.*;   
  
public class Server2 {   
   
	private String server_name;
	private boolean is_exit;
    //List<ClientThread> clients = new ArrayList<ClientThread>();
    Map<String, ClientThread> clients2 = new HashMap<String, ClientThread>();
       
    public static void main(String[] args) {   
        new Server2().start();   
    }   

    /**  
     * 整台服务器开始运行的程序...    
     */  
    public void start(){
    	is_exit = false;
    	
        try {
            boolean iConnect = false;   
            ServerSocket serversocket = new ServerSocket(9000);   
            iConnect = true;
            System.out.println("\n... !注意客户端如果想要成功连接服务器，必须首先输入自己的用户名! ...\n");
            while(iConnect){
            	System.out.print("\n... ^_^ 服务器运行中，正在监听端口 9000 ^_^ ...\n");               
            	Socket s = serversocket.accept();
                
            	//创建 客户端子线程
            	ClientThread currentClient = new ClientThread(s);   
                
            	//把 当前客户端 加入到在线的所有客户端的List中
            	clients2.put(server_name, currentClient);

                System.out.println("^_^ 有新的客户端通过连接 ^_^\n已经连接的客户端数目是："+ clients2.size());
                // 启动客户端
                new Thread(currentClient).start();
            }
        } catch (IOException e) { e.printStackTrace();}
        
    }
    
    /**  
     * 用于专门管理每个客户端的子进程   
     */  
    class ClientThread implements Runnable {   
    	private String user_name;
    	private Socket s;
        private DataInputStream dis;   
        private DataOutputStream dos;   
        private String str;   
        private boolean iConnect = false;   
        
        ClientThread(Socket s)
        {
        	this.s = s; iConnect = true;
        	try {
            	dis = new DataInputStream(s.getInputStream());
            	user_name = dis.readUTF();
            	
            	// 将用户上线信息转发给所有的客户端(包括自己)
            	for(String name:clients2.keySet()){
                		ClientThread c = clients2.get(name);
                		c.sendMsg("\n"+user_name+" enter!\n"); 
                }
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	server_name = user_name;
        }
           
        public void run(){   
            try {
            	is_exit = false;
            	
                while(iConnect){
                	dis = new DataInputStream(s.getInputStream());
                    str = dis.readUTF();
                    System.out.println("客户端发送过来的信息是"+ str);   
                    
                    SimpleDateFormat time = new SimpleDateFormat("MM-dd/HH:mm:ss");	// 设置日期的格式
                    str = time.format(new Date()) + " " + s.getInetAddress().getHostAddress() + " " + s.getPort()+ "\n"+str;
                    
                    // 在这里对当前在线的各个客户端（包括本身） 转发 经过服务器处理之后的消息
                	for(String name:clients2.keySet()){
                    		ClientThread c = clients2.get(name);
                    		c.sendMsg(user_name+": "+ str); 
                    }
                }
            } 
            catch (IOException e) {
            	
            	Set<String > all_user = clients2.keySet();
            	System.out.print("------------------------\n");
            	System.out.println("有客户退出,退出前所有用户列表：");
            	for(String name:all_user){
            		System.out.println(name);
            	}
            	System.out.print("----------\n");
            	
            	for(String name:all_user){
            		// 发送退出信号，但不发送给自己
            		if (! name.equals(user_name)){
            			ClientThread c = clients2.get(name);
            			c.sendMsg("\n"+ user_name + " exit!");
            		} else {
                		server_name = name;
                		is_exit = true;
                	}
            	} 
            }

            if (is_exit){
            	clients2.remove(server_name);
        		System.out.println(server_name+ " 客户端退出了\n已经连接的客户端数目是："+ clients2.size());
        		System.out.print("------------------------\n");
            }
        }   
           
        /**    
         * 将送至服务器的消息发送给每个连接到的客户端  
         */  
        public void sendMsg(String str){
        	
            try {   
                dos = new DataOutputStream(this.s.getOutputStream());   
                dos.writeUTF(str);       
            } catch (IOException e) { e.printStackTrace();}
            
        }
    }   
}