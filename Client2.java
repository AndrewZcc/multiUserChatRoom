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
     * �����ͻ��˵ĳ������
     */  
    public static void main(String[] args) {
    	Client2 client = new Client2();
    	client.connect();
        
    	System.out.println("\n...... ^_^ -----------------------------------  ^_^ ......");
    	System.out.println("...... ^_^ �������ӷ������ɹ�����ͺ��������  ^_^ ......");
    	System.out.println(  "...... ^_^       ������ quit �˳����ӣ�        ^_^ ......\n");
		while (true){
			try {

			/*------------- �������ݸ������� -----------------*/
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
		
		System.out.println("\n...... ^_^ �����˳�������������ӣ����߳ɹ���������������Ϣ��  ^_^ ......");
		System.out.println(".........      ^_^ �����������ͻ��˷��͸�����������Ϣ  ^_^ ..........");
    }
    
    /**  
     * �ú����������ӷ�������...  
     */  
    public void connect() {   
    	
        try {
        	s = new Socket("127.0.0.1", 9000);
            dos = new DataOutputStream(s.getOutputStream());   
            dis = new DataInputStream(s.getInputStream());
            
            System.out.print("^_^ �����������û���: ");
            user_name = scanner.nextLine();
            System.out.println("^_^ �����û�����: "+user_name);
            try { dos.writeUTF(user_name);} catch (IOException e){e.printStackTrace();}
            
            new Thread(new SendThread()).start();
            
        } catch (UnknownHostException e) {   
            System.out.println("UnknownHostException");   
            e.printStackTrace();   
        } catch (IOException e) {   
            System.out.println("IOException");   
            e.printStackTrace();   
        }finally{   
            //�ر�...
        }   
    }
    
    /**  
     * �����߳�ר������ �ͻ��� ������Ϣ  
     */  
    class SendThread implements Runnable{   
        private String str;   
        private boolean iConnect = false;   
           
        public void recMsg() {   
            
        	try {   
                while(iConnect){
                	
                /*------------- �ӷ������������� ����ӡ���� -----------------*/
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
