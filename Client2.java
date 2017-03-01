import java.io.*;   
import java.util.Scanner;
import java.net.*;   
  
public class Client2 {
	private static String user_name;
	private Socket s;
    private static DataOutputStream dos;   
    private DataInputStream dis;
    private static Scanner scanner = new Scanner(System.in);
    
    /**  
     * 整个客户端的程序入口
     */  
    public static void main(String[] args) {
    	Client2 client = new Client2();
    	client.connect();
        
    	System.out.println("\n...... ^_^ -----------------------------------  ^_^ ......");
    	System.out.println("...... ^_^ 您已连接服务器成功，快和好友聊天吧  ^_^ ......");
    	System.out.println(  "...... ^_^       ！输入 quit 退出连接！        ^_^ ......\n");
		while (true){
			try {

			/*------------- 发送数据给服务器 -----------------*/
				//Scanner scanner = new Scanner(System.in);
				String s1 = scanner.nextLine();

				if (s1.equals("quit")) break;
				dos.writeUTF(s1);   
		    /*----------------------------------------------*/
				
			} catch (IOException e1) {   
				System.out.println("IOException");   
				e1.printStackTrace();   
			}
		}
		
		System.out.println("\n...... ^_^ 您已退出与服务器的连接，下线成功，不能再输入信息了  ^_^ ......");
		System.out.println(".........      ^_^ 以下是其他客户端发送给您的离线信息  ^_^ ..........");
    }
    
    /**  
     * 该函数用于连接服务器中...  
     */  
    public void connect() {   
    	
        try {
        	s = new Socket("127.0.0.1", 9000);
            dos = new DataOutputStream(s.getOutputStream());   
            dis = new DataInputStream(s.getInputStream());
            
            System.out.print("^_^ 请输入您的用户名: ");
            user_name = scanner.nextLine();
            System.out.println("^_^ 您的用户名是: "+user_name);
            try { dos.writeUTF(user_name);} catch (IOException e){e.printStackTrace();}
            
            new Thread(new SendThread()).start();
            
        } catch (UnknownHostException e) {   
            System.out.println("UnknownHostException");   
            e.printStackTrace();   
        } catch (IOException e) {   
            System.out.println("IOException");   
            e.printStackTrace();   
        }finally{   
            //关闭...
        }   
    }
    
    /**  
     * 定义线程专门用于 客户端 接收消息  
     */  
    class SendThread implements Runnable{   
        private String str;   
        private boolean iConnect = false;   
           
        public void recMsg() {   
            
        	try {   
                while(iConnect){
                	
                /*------------- 从服务器接收数据 并打印出来 -----------------*/
                    str = dis.readUTF();   
                    System.out.println(str);
                /*--------------------------------------------------------*/
                
                }   
            } catch (IOException e) {   
                e.printStackTrace();   
            }   
        }
        
        public void run(){   
            iConnect = true;   
            recMsg();              
        }     
    }
    
}
