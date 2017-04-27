package databaseManagers;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import java.awt.Color;

public class Libreria extends JFrame implements ActionListener,WindowListener {

	private JPanel contentPane;
	private JTextField txtTema;
	private JTextField txtEstante;
	private JTextField txtEjemplares;
	private JButton btnPrimero;
	private JButton btnAnterior;
	private JButton btnSiguiente;
	private JButton btnUltimo;
	private JButton btnInsertar, btnBorrar, btnLimpiar;
	private JTextField txtNavegador;
	private Connection conn;
	private ResultSet rset;
	private Statement stmt;
	private int totalregistros;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Libreria frame = new Libreria();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Libreria() {
		setBackground(new Color(51, 153, 153));
		setForeground(new Color(0, 153, 153));
		setTitle("Libreria");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 347, 249);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setForeground(new Color(32, 178, 170));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		this.setLocationRelativeTo(null);
		this.addWindowListener(this);
		
		JLabel lblNewLabel = new JLabel("Tema");
		lblNewLabel.setBounds(10, 11, 100, 14);
		contentPane.add(lblNewLabel);
		
		txtTema = new JTextField();
		txtTema.setEditable(false);
		txtTema.setBounds(10, 36, 108, 20);
		contentPane.add(txtTema);
		txtTema.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Estante");
		lblNewLabel_1.setBounds(128, 11, 59, 14);
		contentPane.add(lblNewLabel_1);
		
		txtEstante = new JTextField();
		txtEstante.setEditable(false);
		txtEstante.setBounds(128, 36, 92, 20);
		contentPane.add(txtEstante);
		txtEstante.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("Ejemplares");
		lblNewLabel_2.setBounds(230, 11, 79, 14);
		contentPane.add(lblNewLabel_2);
		
		txtEjemplares = new JTextField();
		txtEjemplares.setEditable(false);
		txtEjemplares.setBounds(230, 36, 86, 20);
		contentPane.add(txtEjemplares);
		txtEjemplares.setColumns(10);
		
		btnPrimero = new JButton("<<");
		btnPrimero.setBounds(10, 78, 49, 23);
		contentPane.add(btnPrimero);
		btnPrimero.addActionListener(this);
		
		btnAnterior = new JButton("<");
		btnAnterior.setBounds(69, 78, 49, 23);
		contentPane.add(btnAnterior);
		btnAnterior.addActionListener(this);
		
		btnSiguiente = new JButton(">");
		btnSiguiente.setBounds(210, 78, 49, 23);
		contentPane.add(btnSiguiente);
		btnSiguiente.addActionListener(this);
		
		btnUltimo = new JButton(">>");
		btnUltimo.setBounds(272, 78, 49, 23);
		contentPane.add(btnUltimo);
		btnUltimo.addActionListener(this);
		
		txtNavegador = new JTextField();
		txtNavegador.setHorizontalAlignment(SwingConstants.CENTER);
		txtNavegador.setEditable(false);
		txtNavegador.setBounds(122, 79, 84, 20);
		contentPane.add(txtNavegador);
		txtNavegador.setColumns(10);
		
		btnLimpiar = new JButton("Limpiar Campos");
		btnLimpiar.setBounds(100, 164, 132, 25);
		contentPane.add(btnLimpiar);
		btnLimpiar.addActionListener(this);
		btnLimpiar.setEnabled(false);
		
		btnInsertar = new JButton("Insertar tema");
		btnInsertar.setBounds(10, 128, 123, 25);
		contentPane.add(btnInsertar);
		btnInsertar.addActionListener(this);
		
		btnBorrar = new JButton("Borrar tema");
		btnBorrar.setBounds(193, 128, 123, 25);
		contentPane.add(btnBorrar);
		btnBorrar.addActionListener(this);
	}
	
	
	
	
	private void conectar(){
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			this.conn = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:xe", "java", "java");
		} catch (ClassNotFoundException e) {
			JOptionPane.showMessageDialog(null, "No se encuentra el driver de Base de Datos");
			System.exit(0);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			System.exit(0);
		}

	}
	
	private void desconectar(){
		try {
			this.conn.close();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}
	
	private void totalRegistros(){
		String sql="Select count(*) from LIBRERIA";
		try {
			Statement stmt=conn.createStatement();
			ResultSet rset=stmt.executeQuery(sql);
			rset.next();
			totalregistros=rset.getInt(1);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}
	private void cargarDatos(){
		String sql="Select TEMA,ESTANTE,EJEMPLARES from LIBRERIA";
		try {
			stmt=conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			rset=stmt.executeQuery(sql);
			rset.next();
			totalRegistros();
			mostrarDatos();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}
	
	private void mostrarDatos() throws SQLException{
		txtTema.setText(rset.getString("TEMA"));
		txtEstante.setText(rset.getString("ESTANTE"));
		txtEjemplares.setText(""+rset.getInt("EJEMPLARES"));
		controlarBotonesNavegacion();
		txtNavegador.setText(rset.getRow()+"/"+totalregistros);
	}
	
	private void controlarBotonesNavegacion() throws SQLException{
		btnPrimero.setEnabled(true);
		btnAnterior.setEnabled(true);
		btnUltimo.setEnabled(true);
		btnSiguiente.setEnabled(true);
		if(rset.isFirst()){
			btnPrimero.setEnabled(false);
			btnAnterior.setEnabled(false);
		}
		if(rset.isLast()){
			btnUltimo.setEnabled(false);
			btnSiguiente.setEnabled(false);
		}
	}
	private void setEditableTxt(boolean activo){
		txtTema.setEditable(activo);
		txtEstante.setEditable(activo);
		txtEjemplares.setEditable(activo);
	}
	
	private void setEnableBotones(boolean activo){
		btnPrimero.setEnabled(activo);
		btnAnterior.setEnabled(activo);
		btnUltimo.setEnabled(activo);
		btnSiguiente.setEnabled(activo);
		btnLimpiar.setEnabled(activo);
		btnInsertar.setEnabled(activo);
		btnBorrar.setEnabled(activo);
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Apéndice de método generado automáticamente
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Apéndice de método generado automáticamente
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		desconectar();
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Apéndice de método generado automáticamente
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Apéndice de método generado automáticamente
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Apéndice de método generado automáticamente
		
	}

	@Override
	public void windowOpened(WindowEvent e) {
		conectar();
		cargarDatos();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource().equals(btnSiguiente)){
			try {
				rset.next();
				mostrarDatos();
			} catch (SQLException e1) {
				JOptionPane.showMessageDialog(null, e1.getMessage());
			}
			return;
		}
		
		if(e.getSource().equals(btnAnterior)){
			try {
				rset.previous();
				mostrarDatos();
			} catch (SQLException e1) {
				JOptionPane.showMessageDialog(null, e1.getMessage());
			}
			return;
		}
		
		if(e.getSource().equals(btnPrimero)){
			try {
				rset.first();
				mostrarDatos();
			} catch (SQLException e1) {
				JOptionPane.showMessageDialog(null, e1.getMessage());
			}
			return;
		}
		if(e.getSource().equals(btnUltimo)){
			try {
				rset.last();
				mostrarDatos();
			} catch (SQLException e1) {
				JOptionPane.showMessageDialog(null, e1.getMessage());
			}
			return;
		}
		
		if(e.getSource().equals(btnInsertar)){
			if(btnInsertar.getText().equals("Nuevo")){
				setEditableTxt(true);
				setEnableBotones(false);
				btnLimpiar.setEnabled(true);
				btnInsertar.setEnabled(true);
				btnInsertar.setText("Guardar");
				txtTema.setText("");
				txtEstante.setText("");
				txtEjemplares.setText("");
				txtNavegador.setText("Nuevo");
			}else{
				//guardar datos
				String sql="Insert into LIBRERIA (TEMA,ESTANTE,EJEMPLARES) "
						+ "values ('"+txtTema.getText()+"','"+txtEstante.getText()+"',"+txtEjemplares.getText()+")";
				
				try {
					rset.close();
					stmt.executeUpdate(sql); 
				} catch (SQLException e1) {
					if(e1.getErrorCode()==1){
						JOptionPane.showMessageDialog(null, "El tema "+txtTema.getText()+" ya existe");
					}else{
						JOptionPane.showMessageDialog(null, e1.getMessage());
					}
				}
				setEditableTxt(false);
				setEnableBotones(true);
				btnInsertar.setText("Nuevo");
				cargarDatos();
			}
			return;
		}
		
		if(e.getSource().equals(btnBorrar)){
			int ret=JOptionPane.showConfirmDialog(null, "Esta seguro que quiere eliminar el Ejemplar "+txtEjemplares.getText(),"Borrar Ejemplar",JOptionPane.YES_NO_OPTION);
			if (ret==0){
				String sql="delete from LIBRERIA where EJEMPLARES="+txtEjemplares.getText();
				try {
					rset.close();
					ret=stmt.executeUpdate(sql);
					if(ret>0){
						JOptionPane.showMessageDialog(null, "El ejemplar "+txtEjemplares.getText()+" ha sido eliminado");
					}
					cargarDatos();
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage());
				}
				
			}
			return;
		}
		
		
		if(e.getSource().equals(btnLimpiar)){
			txtEjemplares.setText("");
			txtTema.setText("");
			txtEstante.setText("");
			return;
		}
		
	}
	
	

}
