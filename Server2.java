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
     * ��̨��������ʼ���еĳ���...    
     */  
    public void start(){
    	is_exit = false;
    	
        try {
            boolean iConnect = false;   
            ServerSocket serversocket = new ServerSocket(9000);   
            iConnect = true;
            System.out.println("\n... !ע��ͻ��������Ҫ�ɹ����ӷ��������������������Լ����û���! ...\n");
            while(iConnect){
            	System.out.print("\n... ^_^ �����������У����ڼ����˿� 9000 ^_^ ...\n");               
            	Socket s = serversocket.accept();
                
            	//���� �ͻ������߳�
            	ClientThread currentClient = new ClientThread(s);   
                
            	//�� ��ǰ�ͻ��� ���뵽���ߵ����пͻ��˵�List��
            	clients2.put(server_name, currentClient);

                System.out.println("^_^ ���µĿͻ���ͨ������ ^_^\n�Ѿ����ӵĿͻ�����Ŀ�ǣ�"+ clients2.size());
                // �����ͻ���
                new Thread(currentClient).start();
            }
        } catch (IOException e) { e.printStackTrace();}
        
    }
    
    /**  
     * ����ר�Ź���ÿ���ͻ��˵��ӽ���   
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
            	
            	// ���û�������Ϣת�������еĿͻ���(�����Լ�)
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
                    System.out.println("�ͻ��˷��͹�������Ϣ��"+ str);   
                    
                    SimpleDateFormat time = new SimpleDateFormat("MM-dd/HH:mm:ss");	// �������ڵĸ�ʽ
                    str = time.format(new Date()) + " " + s.getInetAddress().getHostAddress() + " " + s.getPort()+ "\n"+str;
                    
                    // ������Ե�ǰ���ߵĸ����ͻ��ˣ��������� ת�� ��������������֮�����Ϣ
                	for(String name:clients2.keySet()){
                    		ClientThread c = clients2.get(name);
                    		c.sendMsg(user_name+": "+ str); 
                    }
                }
            } 
            catch (IOException e) {
            	
            	Set<String > all_user = clients2.keySet();
            	System.out.print("------------------------\n");
            	System.out.println("�пͻ��˳�,�˳�ǰ�����û��б�");
            	for(String name:all_user){
            		System.out.println(name);
            	}
            	System.out.print("----------\n");
            	
            	for(String name:all_user){
            		// �����˳��źţ��������͸��Լ�
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
        		System.out.println(server_name+ " �ͻ����˳���\n�Ѿ����ӵĿͻ�����Ŀ�ǣ�"+ clients2.size());
        		System.out.print("------------------------\n");
            }
        }   
           
        /**    
         * ����������������Ϣ���͸�ÿ�����ӵ��Ŀͻ���  
         */  
        public void sendMsg(String str){
        	
            try {   
                dos = new DataOutputStream(this.s.getOutputStream());   
                dos.writeUTF(str);       
            } catch (IOException e) { e.printStackTrace();}
            
        }
    }   
}