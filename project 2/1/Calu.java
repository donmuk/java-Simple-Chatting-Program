package calu;
import java.awt.*;
import java.awt.event.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
/*
 *  ������ �Ǵ� Ŭ���� Calu ���� Ŭ���� Calculator
 *  ������ + - / * ������ �Ҽ��ִ�.
 *  �۵����� �ǿ����� ������ �ǿ����� = �������� ����
 *  �ǿ����� Ŭ���� ������ Ŭ���� �ؽ�Ʈ Area�� �ʱ�ȭ�ǰ�  CalculatorŬ�������ִ� calculateBi�޼ҵ�� ���ڸ� �Ѱ��ش�.
 *  ���� �ǿ����� Ŭ���� = Ŭ���� ����ϴ� �ý���
 *  reader �޼ҵ带 ���Ͽ� �Ǽ������� ������ش�.
 */
public class Calu extends Frame implements ActionListener{ 
	public static void main(String[] args) {
		Calu cal = new Calu();
		cal.init();
	}
	private JFrame frame;
    private JPanel panel;
    private JTextArea text;
    private JButton B[], BAdd, BMinus, BMultiply, BDivide,BEqual, BCancel;
    private Calculator cal;
    private final String[] BValue = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };

	public Calu() {
		super();
		setLayout(new BorderLayout());
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				System.exit(0);
			}
		});
		 frame = new JFrame("Calculator");
		 frame.setResizable(false);
	     panel = new JPanel(new FlowLayout());

	     text = new JTextArea(2, 25);
	     B = new JButton[10];
	     for (int i = 0; i < 10; i++) {
	    	 B[i]= new JButton(String.valueOf(i));
	     }
	     BAdd = new JButton("+");
	     BMinus = new JButton("-");
	     BMultiply = new JButton("*");
	     BDivide = new JButton("/");
	     BEqual = new JButton("=");
	     BCancel = new JButton("C");
		 cal = new Calculator();
	}
	public void init() { //��갡 Gui ����
		frame.setVisible(true);
        frame.setSize(270, 210);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        panel.add(text);
       
        for (int i = 1; i < 10; i++) {
            panel.add(B[i]);
            B[i].addActionListener(this);
        }
        panel.add(B[0]);
        B[0].addActionListener(this);
        
        panel.add(BAdd);
        panel.add(BMinus);
        panel.add(BMultiply);
        panel.add(BDivide);
        panel.add(BEqual);
        panel.add(BCancel);

        BAdd.addActionListener(this);
        BMinus.addActionListener(this);
        BMultiply.addActionListener(this);
        BDivide.addActionListener(this);
        BEqual.addActionListener(this);
        BCancel.addActionListener(this);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object source = e.getSource();
		for(int i=0;i<10;i++) {
			if(source == B[i]) {
				text.replaceSelection(BValue[i]);
				return ;
			}
		}
		if(source == BAdd) {
			writer(cal.calculateBi(Calculator.BiOperatorModes.add, reader()));
		}
		if(source == BMinus) {
			writer(cal.calculateBi(Calculator.BiOperatorModes.minus, reader()));
		}
		if(source == BMultiply) {
			writer(cal.calculateBi(Calculator.BiOperatorModes.multiply, reader()));
		}
		if(source == BDivide) {
			writer(cal.calculateBi(Calculator.BiOperatorModes.divide, reader()));
		}
		if(source == BEqual) {
			writer(cal.calculateEqual(reader()));
		}
		if(source == BCancel) {
			writer(cal.reset());
		}
		text.selectAll();
	}
	public Double reader() { //�Ǽ��� ���
        Double num;
        String str;
        str = text.getText();
        num = Double.valueOf(str);

        return num;
    }
	public void writer(Double num) { //�Է�
        if (Double.isNaN(num)) {
            text.setText("");
        } else {
            text.setText(Double.toString(num));
        }
    }
}

