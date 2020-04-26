
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import javax.swing.BoxLayout;
import javax.swing.JFrame;

class Checksum extends JFrame implements ActionListener{

	JTextField textfield[];
	JLabel label[];
	JPanel panel;
	JButton senderBt;
	JButton exitBt;
	
	public Checksum() {
		panel = new JPanel();
		textfield = new JTextField[2];
		label = new JLabel[2];
		panel.setLayout(new GridLayout(0,2));
		for(int i=0; i<2; i++) {
			label[i] = new JLabel();
	
			textfield[i] = new JTextField();
			textfield[i].setEditable(false);
			panel.add(label[i]);
			panel.add(textfield[i]);		
		}
		label[0].setText("  Sender: ���� Ȯ��");
		label[1].setText("  Sender: CheckSum");
		senderBt = new JButton("CheckSum ����");
		exitBt = new JButton("����");
		panel.add(senderBt);
		panel.add(exitBt);
		senderBt.addActionListener(this);
		exitBt.addActionListener(this);
		mainFrame();
	}
	
	public void mainFrame() {
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("CheckSum");
		setSize(400,150);
		p.add(panel);
		add(p);
		setVisible(true);
		System.out.println("�Է� ������ Ȯ���ϰ� Checksum�� �����ϼ���.");
		System.out.println("�Է�����: input.txt");
		System.out.println("�������: output_checksum.txt");
	}

	
	public static void main(String[] args) {
		Checksum algorigm = new Checksum();
		
	}
	
	public String getFile() {
		try {	
			File file = new File("./input.txt");
			FileReader filereader = new FileReader(file);
			int ch = 0;
			StringBuilder checksum = new StringBuilder("");
			StringBuilder tempsum = new StringBuilder("");
			while(true) {
				ch = filereader.read();
				if(bitSum(ch, checksum, tempsum) == false)
					break;
			}
			complementTrans(checksum);
			textfield[0].setText("  ���� ã�� ����");
			filereader.close();
			return checksum.toString();
		} catch (FileNotFoundException e) {
			textfield[0].setText("  ���� ã�� ����");
			System.out.println("������ ã�� �� �����ϴ�. �Է������� �������ּ���.");
		} catch(IOException e) {
			System.out.println(e);
		}
		return "";
	}
	
	public void writeFile(String checksum) {
		try {
		    OutputStream output = new FileOutputStream("./output_checksum.txt.txt");
		    byte[] by=checksum.getBytes();
		    output.write(by);
				
		} catch (Exception e) {
	            e.getStackTrace();
		}
	}
	
	public Boolean bitSum(int ch, StringBuilder checksum, StringBuilder tempsum) {
		if(ch == -1 && tempsum.length() == 8)
			tempsum = tempsum.append("00000000");
		else if(ch == -1)
			return false;
		else
			tempsum = tempsum.append(toBinary((char)ch));
		if(tempsum.length() == 16) {
			if(checksum.length() == 0) {
				checksum.append(tempsum);
			}else {
				int carry = 0;
				for(int i=15; i>=0; i--) {
					int sum = (checksum.charAt(i) - '0') + (tempsum.charAt(i) - '0') + carry;
			
					if(sum >= 2) {
						char value = (char)(sum - 2 + '0');
						checksum.setCharAt(i, value);
						carry = 1;
					}else {
						checksum.setCharAt(i, (char)(sum + '0'));
						carry = 0;
					}
					
					if(carry == 1 && i == 0) {
						upCarry(checksum, carry);
					}
				}
			}
			System.out.println("now checksum: " + checksum);
			tempsum.setLength(0);
		}
		return true;
	}
	
	public void upCarry(StringBuilder checksum, int carry) {
		for(int j=15; j>=0; j--) {
			int sum = (checksum.charAt(j) - '0') + carry;
	
			if(sum >= 2) {
				char value = (char)(sum - 2 + '0');
				checksum.setCharAt(j, value);
				carry = 1;
			}else {
				checksum.setCharAt(j, (char)(sum + '0'));
				carry = 0;
				break;
			}				
		}
	}
	
	public void complementTrans(StringBuilder checksum) {
		if(checksum.length() != 0) {
			for (int i=15; i>=0; i--) {
				if(checksum.charAt(i) == '0')
					checksum.setCharAt(i, '1');
				else
					checksum.setCharAt(i, '0');
			}
		}
	}
	
	public String toBinary(char character)
    {
		try {
       char []bit = {'0','0','0','0','0','0','0','0'};
       int v = (int)character & 0xFFFF;
       int numck = 0;
       for (int idx = 0; v > 0; v >>= 1, idx++) {
          if ((v & 1) == 1) {
             bit[6-idx] = '1';
             numck++;
          }
       }
       if (numck % 2 == 1)
    	   bit[7] = '1';
      return new String(bit);
		} catch(Exception e) {
			System.out.println("�Է����� ������ ������ ������ Ȯ�����ּ���. �����ڿ� ���ڸ� �Է��� �� �ֽ��ϴ�.");
			System.exit(0);
			return "";
		}
    }
   
    
    public void actionPerformed(ActionEvent e) {
    	if(e.getSource() == senderBt) {
    		System.out.println("\nCheckSum Log");
    		System.out.println("================================");
    		String cs = getFile();
    		if(cs.length() != 0) {
    			writeFile(cs);
    			System.out.println("================================");
        		System.out.println("final checksum: " + cs + "\n");
        		System.out.println("checksum�� ����Ǿ����ϴ�. ��������� �Է����ϰ� ���� ��ο� ����˴ϴ�.");
        		textfield[1].setText("  " + cs);
    		}
    		else {
    			textfield[1].setText("  üũ���� ������ �� �����ϴ�.");
    			System.out.println("üũ���� ������ �� �����ϴ�. �Է������� �����͸� Ȯ�����ּ���.");
    		}
    		
    		
    	}
    	else if(e.getSource() == exitBt){
    		System.exit(0);	
    	}
    }
    
 
}

