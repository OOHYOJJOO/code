package naper_server;
import java.sql.*;

import javax.swing.*;

import java.awt.*;
import java.util.*;
import javax.swing.table.*; 
import java.awt.event.*;

public class GuestDB extends JFrame implements ActionListener {
	JLabel label,num, id, pw, name, phone, address, resttime;	
	JTextField fnum, fid, fpw, fname, fphone, faddress, fresttime, search;
	JButton add,delete,update,clear,all,sbutton;
	String items[] = {"회원ID","회원이름","전화번호"};
	JComboBox combo;
	JPanel bottompanel,rightpanel,center, pnum, pid, ppw, pname,pphone,paddress, presttime;

	JScrollPane sp;	
	Vector out,title, noresult, masg;
	JTable table;
	DefaultTableModel model;

	String connect = new String("jdbc:mysql://localhost/member");
	String user = new String("root");
	String passwd = new String("root");

	Connection conn;
	Statement stat;
	PreparedStatement insert_Query, delete_Query, updete_Query, query1;
	ResultSet rs;

	public GuestDB(){
		makeGui(); 
		prepareDB();

		select(null);
		model.setDataVector(out, title);
	}

	public void makeGui(){
		label = new JLabel("Guest Manager",JLabel.CENTER);
		
		num = new JLabel("번      호");
		fnum = new JTextField(15);
		pnum = new JPanel();
		pnum.add(num);
		pnum.add(fnum);
		fnum.setEditable(false);		
		
		name = new JLabel("  회원 이름 : ");
		fname = new JTextField(15);
		pname = new JPanel();
		pname.add(name);
		pname.add(fname);
		
		id = new JLabel(" 회원  ID :  ");
		fid = new JTextField(15);
		pid = new JPanel();
		pid.add(id);
		pid.add(fid);
		
		pw = new JLabel("비밀번호 : ");
		fpw = new JTextField(15);
		ppw = new JPanel();
		ppw.add(pw);
		ppw.add(fpw);


		phone = new JLabel(" 전화 번호 : ");
		fphone = new JTextField(15);
		pphone = new JPanel();
		pphone.add(phone);
		pphone.add(fphone);

		address = new JLabel("주        소 : ");
		faddress = new JTextField(15);
		paddress = new JPanel();
		paddress.add(address);
		paddress.add(faddress);
		
		resttime = new JLabel("남은 시간 : ");
		fresttime = new JTextField(15);
		presttime = new JPanel();
		presttime.add(resttime);
		presttime.add(fresttime);

		rightpanel = new JPanel();
		rightpanel.setLayout(new GridLayout(7,1));
	
		rightpanel.add(pnum);
		rightpanel.add(pname);
		rightpanel.add(pid);
		rightpanel.add(ppw);
		rightpanel.add(pphone);
		rightpanel.add(paddress);
		rightpanel.add(presttime);


		add = new JButton("추가");
		add.addActionListener(this);
		delete = new JButton("삭제");
		delete.addActionListener(this);
		update = new JButton("수정");
		update.addActionListener(this);

		combo = new JComboBox(items);

		search = new JTextField(15);
		search.addActionListener(this);

		sbutton = new JButton("검색");
		sbutton.addActionListener(this);

		clear = new JButton("지우기");
		clear.addActionListener(this);
		all = new JButton("전체보기");
		all.addActionListener(this);

		bottompanel = new JPanel();
		bottompanel.add(add);
		bottompanel.add(delete);
		bottompanel.add(update);
		bottompanel.add(combo);
		bottompanel.add(search);
		bottompanel.add(sbutton);
		bottompanel.add(clear);
		bottompanel.add(all);

		title = new Vector();
		out = new Vector();		
		noresult = new Vector();		
		masg = new Vector();		

		title.add("번호");
		title.add("이름");
		title.add("ID");
		title.add("PW");
		title.add("전화번호");
		title.add("주소");
		title.add("남은시간");

		noresult.add("실행 결과");

		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent w) {
				try {
					rs.close();
					stat.close();
					conn.close();
					setVisible(false);
					dispose();
					System.exit(0);
				} catch (Exception e) {
					e.printStackTrace();
				}

				super.windowClosing(w);
			}
		});		

		Container c = getContentPane();

		model = new DefaultTableModel();
		table = new JTable(model);
		table.setRowHeight(20);
		
		
	
		table.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent me){
				int index = table.getSelectedRow();

				Vector masg = (Vector) out.get(index);

				String num = (String) masg.get(0);
				String name = (String) masg.get(1);
				String id = (String) masg.get(2);
				String pw = (String) masg.get(3);
				String phone = (String) masg.get(4);	
				String address = (String) masg.get(5);
				String resttime = (String)masg.get(6);

				fnum.setText(num);
				fname.setText(name);
				fid.setText(id);
				fpw.setText(pw);
				fphone.setText(phone);
				faddress.setText(address);
				fresttime.setText(resttime);

				fid.setEditable(false);
			}
		});

		sp = new JScrollPane(table);
        
		center = new JPanel();
		center.setBounds(300,300,1000,1000);
		center.add(rightpanel);
		center.add(sp);

		c.add(label,"South");
		c.add(center,"Center");	
		c.add(bottompanel,"North");					
	}

	public void prepareDB(){
		try {
			Class.forName("com.mysql.jdbc.Driver");

			conn = DriverManager.getConnection(connect, user, passwd);

			stat = conn.createStatement();

			insert_Query = conn.prepareStatement("insert into member(name,id,pw,phone,address,resttime) values(?,?,?,?,?,?)");
			delete_Query = conn.prepareStatement("delete from member where num = ?");
			updete_Query = conn.prepareStatement("update member set name=?,phone=?,address=?" +
					" where id = ?");
			query1 = conn.prepareStatement("alter table member auto_increment=1;");

		} catch (Exception e) {
		}
	}

	public void add(){
		String name = fname.getText();
		String id = fid.getText();
		String pw = fpw.getText();
		String phone = fphone.getText();
		String address = faddress.getText();
		String resttime = fresttime.getText();

		try {
			insert_Query.setString(1, name);
			insert_Query.setString(2, id);
			insert_Query.setString(3, pw);
			insert_Query.setString(4, phone);
			insert_Query.setString(5, address);
			insert_Query.setString(6, resttime);

			insert_Query.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void delete(){
		String num = fnum.getText();
		try {
			delete_Query.setString(1, num);
			delete_Query.executeUpdate();
			query1.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void update(){

		String name = fname.getText();
		String id = fid.getText();
		String pnumber = fphone.getText();
		String address = faddress.getText();

		try {
			updete_Query.setString(1, name);
			updete_Query.setString(2, pnumber);
			updete_Query.setString(3, address);
			updete_Query.setString(4, id);
			updete_Query.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void search(){
		String keyword = search.getText();
		int i = combo.getSelectedIndex();
		String index;

		if(i==0)
			index = "id";
		else if(i==1)
			index = "name";
		else 
			index = "pnumber";

		String q = "select * from member where "+index+"='"+keyword+"';";
		select(q);
		search.setText("");
	}

	public void clear(){
		fnum.setText("");
		fname.setText("");
		fid.setText("");
		fpw.setText("");
		fphone.setText("");
		faddress.setText("");
		fresttime.setText("");
		
		fid.setEditable(true);
		fnum.setEditable(false);
	}

	public void select(String query){

		try{
			if(query == null)
				query = "select * from member order by num asc;";

			rs = stat.executeQuery(query);

			out.clear();
			while (rs.next()) {
				masg = new Vector<String>();

				masg.add(rs.getString(1));
				masg.add(rs.getString(2));
				masg.add(rs.getString(3));
				masg.add(rs.getString(4));
				masg.add(rs.getString(5));
				masg.add(rs.getString(6));
				masg.add(rs.getString(7));

				out.add(masg);
			}

		}catch(Exception e){}
	}

	public void actionPerformed(ActionEvent w){
		Object o = w.getSource();

		try{
			if(o==add){ 
				if(fid.getText().length() > 0)
					add();
					select(null);

			}else if(o==delete){ 
				if(fid.getText().length() > 0)
					delete();
					select(null);
			}else if(o==clear){
				clear();
			}else if(o==all){ 
				select(null);
			}else if(o==update){ 
				if(fid.getText().length() > 0)
					update();
				select(null);

			}else if(o==search || o == sbutton){ 
				search();
			}

			if (out.isEmpty()) {
				out.clear();
				masg.clear();

				masg.add("찾은 데이터가 없습니다...");
				out.add(masg);
				model.setDataVector(out, noresult);
			}else{
				model.setDataVector(out, title);
			}

			clear();

		}catch(Exception ew){
			ew.printStackTrace();
		}
	}

	public static void main(String[] args)	{
		GuestDB f = new GuestDB();
		f.pack();
		f.setVisible(true);	
		
	}
	
	
}
