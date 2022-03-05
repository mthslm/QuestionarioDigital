/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.bean;

import com.toedter.calendar.JDateChooser;
import model.dao.EnderecoDAO;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.plaf.OptionPaneUI;
import javax.swing.table.DefaultTableModel;
import model.dao.AnimalDAO;
import model.dao.EstatisticaDAO;
import model.dao.PessoaDAO;
import model.dao.QuestionarioDAO;

/**
 *
 * @author Matheus
 */
public class Interface extends javax.swing.JFrame {

    int xMouse = 0, yMouse = 0;
    Image icon;
    int numPessoas;
    EstatisticaDAO estdao = new EstatisticaDAO();
    EnderecoDAO enddao = new EnderecoDAO();
    PessoaDAO pdao = new PessoaDAO();
    AnimalDAO adao = new AnimalDAO();
    QuestionarioDAO qdao = new QuestionarioDAO();
    int idpessoa;

    DefaultTableModel resultadoPesquisa;
    DefaultTableModel listaQuestionarios;
    DefaultTableModel listaAnimais;

    public Interface() {
        initComponents();
        jTableAnimais.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(jComboBoxSexoAnimal));
        icon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/imagens/logo.png"));
        setIconImage(icon);
        atualizarEstatisticas();
        enddao.getBairros(jComboBoxBairros);
        enddao.getBairros(jComboBoxBairros);
        resultadoPesquisa = (DefaultTableModel) jTableResultados.getModel();
        listaQuestionarios = (DefaultTableModel) jTableQuestionarios.getModel();
        listaAnimais = (DefaultTableModel) jTableAnimais.getModel();
    }

    public void importarCSV() {
        
    }
    
    public int exportarCSV() {
        try {
            JFileChooser janela = new JFileChooser();
            if (janela.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File arquivo = janela.getSelectedFile();
                FileWriter resultado = new FileWriter(arquivo);
                resultado.write("Nome,Rua,Número,Bairro,Número de Questionários\n");
                for (int i = 0; i < jTableResultados.getRowCount(); i++) {
                    resultado.write(jTableResultados.getValueAt(i, 0) + "," + jTableResultados.getValueAt(i, 1) + "," + jTableResultados.getValueAt(i, 2) + "," + jTableResultados.getValueAt(i, 3) + "," + jTableResultados.getValueAt(i, 4) + "\n");
                }
                resultado.flush();
                resultado.close();
                return 1;
            }
            return 2;
        } catch (IOException ex) {
            Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

    public void atualizarEstatisticas() {
        int tf = estdao.numPessoas();
        totalPessoas.setText(tf + "");
        if (tf != 0) {
            possuemAnimais.setText(((100 * estdao.numAnimais()) / tf) + "%");
            possuemCaixa.setText(((100 * estdao.numCaixa()) / tf) + "%");
            possuemPoco.setText(((100 * estdao.numPoco()) / tf) + "%");
            possuemFossa.setText(((100 * estdao.numFossa()) / tf) + "%");
            totalForms.setText(estdao.numForms() + "");
        }
    }

    public void trocarAba(JPanel aba) {
        abas.removeAll();
        abas.add(aba);
        abas.repaint();
        abas.revalidate();
    }

    public void trocarCriarEditar(JPanel aba) {
        acoes.removeAll();
        acoes.add(aba);
        acoes.repaint();
        acoes.revalidate();
    }

    public void limparFiltros() {
        JCheckBox selecoes[] = {cisterna, cxdagua, poco, fossa, animais, masc, fem, castrado};
        JDateChooser datas[] = {data1, data2};
        JTextField campos[] = {numero, especie, rua, bairro, area, idade};
        for (JCheckBox selecao : selecoes) {
            selecao.setSelected(false);
        }
        for (JDateChooser data : datas) {
            data.setDate(null);
        }
        for (JTextField campo : campos) {
            campo.setText("");
        }
    }

    public void trocarPesquisa(JPanel aba) {
        pesquisa.removeAll();
        pesquisa.add(aba);
        pesquisa.repaint();
        pesquisa.revalidate();
    }

    public void trocarCampoRua(JPanel aba) {
        jPanelRua.removeAll();
        jPanelRua.add(aba);
        jPanelRua.repaint();
        jPanelRua.revalidate();
    }

    public void limparCampos() {
        JCheckBox selecoes[] = {pergunta1, pergunta1p2, pergunta2, pergunta2p1, pergunta3, pergunta3p1, pergunta4, pergunta5};
        JTextField campos[] = {cadastrarNome, cadastrarNumero, cadastrarComplemento};
        cadastrarData.setDate(null);
        pergunta2p2.setValue(0);
        trocarCampoRua(selecaoRua);
        cadastrarArea.setSelectedIndex(0);
        jComboBoxBairros.setSelectedIndex(0);
        jComboBoxRuas.setSelectedIndex(0);

        for (JTextField campo : campos) {
            campo.setText("");
        }
        for (JCheckBox selecao : selecoes) {
            selecao.setSelected(false);
        }
        listaAnimais.setRowCount(0);
    }

    public void preencherFormulario(int id) {
        listaAnimais.setRowCount(0);
        if (!jTableQuestionarios.isShowing()) {
            listaQuestionarios.setRowCount(0);
            jComboBoxBairros.setSelectedItem(pdao.getPessoa(id).getBairro());

            trocarCampoRua(campoRua);
            cadastrarRuaCampo.setText(pdao.getPessoa(id).getRua());
            cadastrarComplemento.setText(pdao.getPessoa(id).getComplemento());
            cadastrarNumero.setText(pdao.getPessoa(id).getNumero());
            cadastrarNome.setText(pdao.getPessoa(id).getNome());
            numQuestionarios.setText(pdao.getPessoa(id).getQuestionario().size() + "");

            for (Questionario quests : pdao.getPessoa(id).getQuestionario()) {
                listaQuestionarios.addRow(new Object[]{quests.getData()});
            }
        }
        if (jTableQuestionarios.getSelectedRow() >= 0 && jTableQuestionarios.isShowing()) {
            cadastrarData.setDate(pdao.getPessoa(id).getQuestionario().get(jTableQuestionarios.getSelectedRow()).getData());
            pergunta1.setSelected(pdao.getPessoa(id).getQuestionario().get(jTableQuestionarios.getSelectedRow()).isCisterna());
            pergunta1p2.setSelected(pdao.getPessoa(id).getQuestionario().get(jTableQuestionarios.getSelectedRow()).isCisternaconsumo());
            pergunta2.setSelected(pdao.getPessoa(id).getQuestionario().get(jTableQuestionarios.getSelectedRow()).isCxdagua());
            pergunta2p1.setSelected(pdao.getPessoa(id).getQuestionario().get(jTableQuestionarios.getSelectedRow()).isTampada());
            pergunta2p2.setValue(pdao.getPessoa(id).getQuestionario().get(jTableQuestionarios.getSelectedRow()).getCapacidade());
            pergunta3.setSelected(pdao.getPessoa(id).getQuestionario().get(jTableQuestionarios.getSelectedRow()).isPcartesiano());
            pergunta3p1.setSelected(pdao.getPessoa(id).getQuestionario().get(jTableQuestionarios.getSelectedRow()).isPococonsumo());
            pergunta4.setSelected(pdao.getPessoa(id).getQuestionario().get(jTableQuestionarios.getSelectedRow()).isFseptica());
            pergunta5.setSelected(pdao.getPessoa(id).getQuestionario().get(jTableQuestionarios.getSelectedRow()).isAnimal());

            for (Animal a : pdao.getPessoa(id).getQuestionario().get(jTableQuestionarios.getSelectedRow()).getAnimais()) {
                listaAnimais.addRow(new Object[]{a.getEspecie(), a.getSexo(), a.isCastrado(), a.getIdade(), a.getChip(), a.getId()});
            }

        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jComboBoxSexoAnimal = new javax.swing.JComboBox<>();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btnHome = new javax.swing.JLabel();
        btnBuscar = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        btnImportar = new javax.swing.JLabel();
        btnExportar = new javax.swing.JLabel();
        btnCadastrar = new javax.swing.JLabel();
        divisor = new javax.swing.JPanel();
        abas = new javax.swing.JPanel();
        home = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        totalPessoas = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        possuemAnimais = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        possuemCaixa = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jPanel20 = new javax.swing.JPanel();
        totalForms = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jPanel21 = new javax.swing.JPanel();
        possuemFossa = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jPanel22 = new javax.swing.JPanel();
        possuemPoco = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        cadastrar = new javax.swing.JPanel();
        cadastrarNome = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        cadastrarNumero = new javax.swing.JTextField();
        jComboBoxBairros = new javax.swing.JComboBox<>();
        jLabel19 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        cadastrarComplemento = new javax.swing.JTextField();
        cadastrarArea = new javax.swing.JComboBox<>();
        jPanelRua = new javax.swing.JPanel();
        selecaoRua = new javax.swing.JPanel();
        jComboBoxRuas = new javax.swing.JComboBox<>();
        jButton11 = new javax.swing.JButton();
        campoRua = new javax.swing.JPanel();
        selecaoRua1 = new javax.swing.JPanel();
        cadastrarRuaCampo = new javax.swing.JTextField();
        jButton10 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        pergunta1 = new javax.swing.JCheckBox();
        jPanel12 = new javax.swing.JPanel();
        pergunta2 = new javax.swing.JCheckBox();
        jLabel5 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        pergunta1p2 = new javax.swing.JCheckBox();
        p1p2 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        p2p1 = new javax.swing.JLabel();
        pergunta2p1 = new javax.swing.JCheckBox();
        jPanel14 = new javax.swing.JPanel();
        p2p2 = new javax.swing.JLabel();
        pergunta2p2 = new javax.swing.JSpinner();
        jPanel15 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        pergunta3 = new javax.swing.JCheckBox();
        jPanel16 = new javax.swing.JPanel();
        p3p1 = new javax.swing.JLabel();
        pergunta3p1 = new javax.swing.JCheckBox();
        jPanel17 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        pergunta4 = new javax.swing.JCheckBox();
        jPanel18 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        pergunta5 = new javax.swing.JCheckBox();
        jPanel19 = new javax.swing.JPanel();
        p5p1 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTableAnimais = new javax.swing.JTable();
        jButton7 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        acoes = new javax.swing.JPanel();
        criar = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        editar = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        numQuestionarios = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTableQuestionarios = new javax.swing.JTable();
        cadastrarData = new com.toedter.calendar.JDateChooser();
        buscar = new javax.swing.JPanel();
        pesquisa = new javax.swing.JPanel();
        endereco = new javax.swing.JPanel();
        numero = new javax.swing.JTextField();
        bairro = new javax.swing.JTextField();
        rua = new javax.swing.JTextField();
        area = new javax.swing.JTextField();
        data = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        data1 = new com.toedter.calendar.JDateChooser();
        data2 = new com.toedter.calendar.JDateChooser();
        animal = new javax.swing.JPanel();
        especie = new javax.swing.JTextField();
        castrado = new javax.swing.JCheckBox();
        masc = new javax.swing.JCheckBox();
        fem = new javax.swing.JCheckBox();
        idade = new javax.swing.JTextField();
        questionario = new javax.swing.JPanel();
        cisterna = new javax.swing.JCheckBox();
        cxdagua = new javax.swing.JCheckBox();
        animais = new javax.swing.JCheckBox();
        fossa = new javax.swing.JCheckBox();
        poco = new javax.swing.JCheckBox();
        jLabel3 = new javax.swing.JLabel();
        pesquisarPor = new javax.swing.JComboBox<>();
        jButton5 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableResultados = new javax.swing.JTable();
        jButton6 = new javax.swing.JButton();
        jLabelNResultados = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jButton8 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();

        jComboBoxSexoAnimal.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Masculino", "Feminino", "Não sei" }));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Formulário Digital");
        setMinimumSize(new java.awt.Dimension(800, 596));
        setUndecorated(true);
        setResizable(false);
        getContentPane().setLayout(null);

        jPanel1.setBackground(new java.awt.Color(30, 37, 46));

        jPanel3.setBackground(new java.awt.Color(34, 44, 50));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/logo-saude-branco (1).png"))); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnHome.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        btnHome.setForeground(new java.awt.Color(46, 204, 113));
        btnHome.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/home-verde.png"))); // NOI18N
        btnHome.setText("Home");
        btnHome.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnHome.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                btnHomeFocusGained(evt);
            }
        });
        btnHome.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnHomeMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnHomeMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnHomeMouseExited(evt);
            }
        });

        btnBuscar.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        btnBuscar.setForeground(new java.awt.Color(231, 230, 233));
        btnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/editar.png"))); // NOI18N
        btnBuscar.setText("Buscar/Editar");
        btnBuscar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBuscar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnBuscarMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnBuscarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnBuscarMouseExited(evt);
            }
        });

        jPanel4.setBackground(new java.awt.Color(34, 44, 50));

        btnImportar.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        btnImportar.setForeground(new java.awt.Color(231, 230, 233));
        btnImportar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/importar.png"))); // NOI18N
        btnImportar.setText("Importar");
        btnImportar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnImportar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnImportarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnImportarMouseExited(evt);
            }
        });

        btnExportar.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        btnExportar.setForeground(new java.awt.Color(231, 230, 233));
        btnExportar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/exportar.png"))); // NOI18N
        btnExportar.setText("Exportar");
        btnExportar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnExportar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnExportarMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnExportarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnExportarMouseExited(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnExportar)
                    .addComponent(btnImportar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnImportar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnExportar)
                .addContainerGap())
        );

        btnCadastrar.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        btnCadastrar.setForeground(new java.awt.Color(231, 230, 233));
        btnCadastrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/cadastrar.png"))); // NOI18N
        btnCadastrar.setText("Cadastrar");
        btnCadastrar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCadastrar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCadastrarMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCadastrarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCadastrarMouseExited(evt);
            }
        });

        divisor.setBackground(new java.awt.Color(46, 204, 113));
        divisor.setPreferredSize(new java.awt.Dimension(2, 254));

        javax.swing.GroupLayout divisorLayout = new javax.swing.GroupLayout(divisor);
        divisor.setLayout(divisorLayout);
        divisorLayout.setHorizontalGroup(
            divisorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 2, Short.MAX_VALUE)
        );
        divisorLayout.setVerticalGroup(
            divisorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 254, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 1, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(divisor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnCadastrar)
                    .addComponent(btnBuscar)
                    .addComponent(btnHome))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 75, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(divisor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(btnHome)
                        .addGap(27, 27, 27)
                        .addComponent(btnCadastrar)
                        .addGap(32, 32, 32)
                        .addComponent(btnBuscar)
                        .addGap(24, 24, 24)))
                .addGap(64, 64, 64)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        getContentPane().add(jPanel1);
        jPanel1.setBounds(0, 0, 260, 600);

        abas.setBackground(new java.awt.Color(231, 230, 233));
        abas.setLayout(new java.awt.CardLayout());

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));

        totalPessoas.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        totalPessoas.setForeground(new java.awt.Color(102, 102, 102));
        totalPessoas.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        totalPessoas.setText("0");

        jLabel11.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(102, 102, 102));
        jLabel11.setText("Pessoas Cadastradas");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(totalPessoas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(totalPessoas)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel11)
                .addContainerGap())
        );

        jLabel13.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(102, 102, 102));
        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/estatísticas.png"))); // NOI18N
        jLabel13.setText("Estatísticas");

        jLabel2.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(153, 153, 153));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Desenvolvido pela Universidade Federal de Uberlândia | mths@ufu.br");

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));

        possuemAnimais.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        possuemAnimais.setForeground(new java.awt.Color(102, 102, 102));
        possuemAnimais.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        possuemAnimais.setText("0%");

        jLabel25.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(102, 102, 102));
        jLabel25.setText("Possuem Animais");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel25)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(possuemAnimais, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(possuemAnimais)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel25)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));

        possuemCaixa.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        possuemCaixa.setForeground(new java.awt.Color(102, 102, 102));
        possuemCaixa.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        possuemCaixa.setText("0%");

        jLabel27.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(102, 102, 102));
        jLabel27.setText("Possuem Caixa d'Água");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel27)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(possuemCaixa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(possuemCaixa)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel27)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel20.setBackground(new java.awt.Color(255, 255, 255));

        totalForms.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        totalForms.setForeground(new java.awt.Color(102, 102, 102));
        totalForms.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        totalForms.setText("0");

        jLabel29.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(102, 102, 102));
        jLabel29.setText("Formulários Cadastrados");

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel29)
                .addContainerGap())
            .addComponent(totalForms, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(totalForms)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel29)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel21.setBackground(new java.awt.Color(255, 255, 255));

        possuemFossa.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        possuemFossa.setForeground(new java.awt.Color(102, 102, 102));
        possuemFossa.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        possuemFossa.setText("0%");

        jLabel31.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(102, 102, 102));
        jLabel31.setText("Possuem fossa séptica");

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel21Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel31)
                .addContainerGap())
            .addComponent(possuemFossa, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(possuemFossa)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel31)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel22.setBackground(new java.awt.Color(255, 255, 255));

        possuemPoco.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        possuemPoco.setForeground(new java.awt.Color(102, 102, 102));
        possuemPoco.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        possuemPoco.setText("0%");

        jLabel33.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(102, 102, 102));
        jLabel33.setText("Possuem poço artes.");

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel22Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel33)
                .addContainerGap())
            .addComponent(possuemPoco, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(possuemPoco)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel33)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel34.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(102, 102, 102));
        jLabel34.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel34.setText("Questionário Digital");

        jLabel35.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(102, 102, 102));
        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel35.setText("Secretaria Municial de Saúde");

        jLabel36.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(102, 102, 102));
        jLabel36.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel36.setText("Prefeitura de Patos de Minas - MG");

        jLabel37.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(102, 102, 102));
        jLabel37.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel37.setText("Em caso de dúvidas, acesse o manual de instruções clicando em \"Central de Ajuda\" ou");

        jLabel38.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(102, 102, 102));
        jLabel38.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel38.setText("envie um email para mths@ufu.br.");

        jLabel39.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/logo-saude-branco (1).png"))); // NOI18N

        jLabel40.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/logoufu.png"))); // NOI18N

        javax.swing.GroupLayout homeLayout = new javax.swing.GroupLayout(home);
        home.setLayout(homeLayout);
        homeLayout.setHorizontalGroup(
            homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(homeLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(homeLayout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jSeparator1))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, homeLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(homeLayout.createSequentialGroup()
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jLabel37, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel38, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel34, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel36, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, homeLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(208, 208, 208)))
                .addContainerGap())
        );
        homeLayout.setVerticalGroup(
            homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(homeLayout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addComponent(jLabel34)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel35)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel36)
                .addGap(18, 18, 18)
                .addGroup(homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 61, Short.MAX_VALUE)
                .addComponent(jLabel37)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel38)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(homeLayout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addGap(18, 18, 18))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, homeLayout.createSequentialGroup()
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)))
                .addGroup(homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addContainerGap())
        );

        abas.add(home, "card2");

        cadastrarNome.setBackground(new java.awt.Color(240, 240, 240));
        cadastrarNome.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        cadastrarNome.setBorder(javax.swing.BorderFactory.createTitledBorder("Nome"));
        cadastrarNome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cadastrarNomeActionPerformed(evt);
            }
        });

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Endereço"));

        cadastrarNumero.setBackground(new java.awt.Color(240, 240, 240));
        cadastrarNumero.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        cadastrarNumero.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder("Rua"), "Número"));
        cadastrarNumero.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cadastrarNumeroActionPerformed(evt);
            }
        });

        jComboBoxBairros.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxBairrosItemStateChanged(evt);
            }
        });
        jComboBoxBairros.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jComboBoxBairrosMouseClicked(evt);
            }
        });

        jLabel19.setText("Bairro");

        jLabel24.setText("Rua");

        cadastrarComplemento.setBackground(new java.awt.Color(240, 240, 240));
        cadastrarComplemento.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        cadastrarComplemento.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder("Complemento"), "Complemento"));
        cadastrarComplemento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cadastrarComplementoActionPerformed(evt);
            }
        });

        cadastrarArea.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11" }));

        jPanelRua.setLayout(new java.awt.CardLayout());

        jComboBoxRuas.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxRuasItemStateChanged(evt);
            }
        });
        jComboBoxRuas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jComboBoxRuasMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jComboBoxRuasMouseEntered(evt);
            }
        });

        jButton11.setText("Campo");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout selecaoRuaLayout = new javax.swing.GroupLayout(selecaoRua);
        selecaoRua.setLayout(selecaoRuaLayout);
        selecaoRuaLayout.setHorizontalGroup(
            selecaoRuaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(selecaoRuaLayout.createSequentialGroup()
                .addComponent(jComboBoxRuas, 0, 172, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton11))
        );
        selecaoRuaLayout.setVerticalGroup(
            selecaoRuaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, selecaoRuaLayout.createSequentialGroup()
                .addGap(0, 1, Short.MAX_VALUE)
                .addGroup(selecaoRuaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxRuas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton11)))
        );

        jPanelRua.add(selecaoRua, "card2");

        campoRua.setLayout(new java.awt.CardLayout());

        jButton10.setText("Seleção");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout selecaoRua1Layout = new javax.swing.GroupLayout(selecaoRua1);
        selecaoRua1.setLayout(selecaoRua1Layout);
        selecaoRua1Layout.setHorizontalGroup(
            selecaoRua1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(selecaoRua1Layout.createSequentialGroup()
                .addComponent(cadastrarRuaCampo, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton10))
        );
        selecaoRua1Layout.setVerticalGroup(
            selecaoRua1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, selecaoRua1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(cadastrarRuaCampo, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
                .addComponent(jButton10))
        );

        campoRua.add(selecaoRua1, "card2");

        jPanelRua.add(campoRua, "card3");

        jLabel6.setText("Área");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel19)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBoxBairros, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel24)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanelRua, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(cadastrarNumero, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cadastrarComplemento)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cadastrarArea, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel19)
                        .addComponent(jComboBoxBairros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel24))
                    .addComponent(jPanelRua, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cadastrarComplemento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cadastrarNumero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cadastrarArea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addContainerGap())))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Perguntas"));

        jLabel4.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(51, 51, 51));
        jLabel4.setText("1. Possui cisterna?");

        pergunta1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        pergunta1.setText("Sim");
        pergunta1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pergunta1MouseClicked(evt);
            }
        });
        pergunta1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pergunta1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(pergunta1))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel4)
                .addComponent(pergunta1))
        );

        pergunta2.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        pergunta2.setText("Sim");
        pergunta2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pergunta2MouseClicked(evt);
            }
        });
        pergunta2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pergunta2ActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(51, 51, 51));
        jLabel5.setText("2. Possui caixa d'água?");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(pergunta2))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(pergunta2)
                .addComponent(jLabel5))
        );

        pergunta1p2.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        pergunta1p2.setText("Sim");
        pergunta1p2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pergunta1p2ActionPerformed(evt);
            }
        });

        p1p2.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        p1p2.setForeground(new java.awt.Color(51, 51, 51));
        p1p2.setText("1.2. P/ consumo humano?");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addComponent(p1p2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 123, Short.MAX_VALUE)
                .addComponent(pergunta1p2))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(pergunta1p2)
                .addComponent(p1p2))
        );

        p2p1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        p2p1.setForeground(new java.awt.Color(51, 51, 51));
        p2p1.setText("2.1. Tampada?");

        pergunta2p1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        pergunta2p1.setText("Sim");
        pergunta2p1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pergunta2p1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addComponent(p2p1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(pergunta2p1))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(p2p1)
                .addComponent(pergunta2p1))
        );

        p2p2.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        p2p2.setForeground(new java.awt.Color(51, 51, 51));
        p2p2.setText("2.2. Capacidade");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addComponent(p2p2)
                .addGap(18, 18, 18)
                .addComponent(pergunta2p2))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(p2p2)
                .addComponent(pergunta2p2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jLabel15.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(51, 51, 51));
        jLabel15.setText("3. Possui poço artesiano?");

        pergunta3.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        pergunta3.setText("Sim");
        pergunta3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pergunta3MouseClicked(evt);
            }
        });
        pergunta3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pergunta3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(pergunta3))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel15)
                .addComponent(pergunta3))
        );

        jPanel16.setForeground(new java.awt.Color(51, 51, 51));

        p3p1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        p3p1.setForeground(new java.awt.Color(51, 51, 51));
        p3p1.setText("3.1. P/ consumo humano?");

        pergunta3p1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        pergunta3p1.setText("Sim");
        pergunta3p1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pergunta3p1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addComponent(p3p1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(pergunta3p1))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(p3p1)
                .addComponent(pergunta3p1))
        );

        jLabel17.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(51, 51, 51));
        jLabel17.setText("4. Possui fossa séptica?");

        pergunta4.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        pergunta4.setText("Sim");
        pergunta4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pergunta4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(pergunta4))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel17)
                .addComponent(pergunta4))
        );

        jLabel18.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(51, 51, 51));
        jLabel18.setText("5. Possui animais?");

        pergunta5.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        pergunta5.setText("Sim");
        pergunta5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pergunta5MouseClicked(evt);
            }
        });
        pergunta5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pergunta5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(pergunta5))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel18)
                .addComponent(pergunta5))
        );

        p5p1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        p5p1.setForeground(new java.awt.Color(51, 51, 51));
        p5p1.setText("5.1. Quais?");

        jTableAnimais.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Espécie", "Sexo", "Castrado", "Idade", "Chip", "ID"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Boolean.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                true, true, true, true, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableAnimais.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableAnimaisMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(jTableAnimais);
        if (jTableAnimais.getColumnModel().getColumnCount() > 0) {
            jTableAnimais.getColumnModel().getColumn(5).setMinWidth(0);
            jTableAnimais.getColumnModel().getColumn(5).setMaxWidth(0);
        }

        jButton7.setText("Adicionar");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton9.setText("Remover");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(p5p1)
                    .addComponent(jButton7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(10, 10, 10)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addComponent(p5p1)
                .addGap(18, 18, 18)
                .addComponent(jButton7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton9)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jPanel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        acoes.setLayout(new java.awt.CardLayout());

        jButton1.setText("Salvar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Limpar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout criarLayout = new javax.swing.GroupLayout(criar);
        criar.setLayout(criarLayout);
        criarLayout.setHorizontalGroup(
            criarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(criarLayout.createSequentialGroup()
                .addContainerGap(17, Short.MAX_VALUE)
                .addGroup(criarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        criarLayout.setVerticalGroup(
            criarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, criarLayout.createSequentialGroup()
                .addContainerGap(289, Short.MAX_VALUE)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        acoes.add(criar, "card2");

        jButton3.setText("Excluir");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Salvar edição");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        numQuestionarios.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        numQuestionarios.setForeground(new java.awt.Color(51, 51, 51));
        numQuestionarios.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        numQuestionarios.setText("0");

        jLabel7.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(51, 51, 51));
        jLabel7.setText("Foram encontrado(s)");

        jLabel8.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(51, 51, 51));
        jLabel8.setText("questionários para esta");

        jLabel10.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(51, 51, 51));
        jLabel10.setText("pessoa. Selecione para");

        jLabel16.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(51, 51, 51));
        jLabel16.setText("qual data editar.");

        jTableQuestionarios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Questionários"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableQuestionarios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableQuestionariosMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(jTableQuestionarios);

        javax.swing.GroupLayout editarLayout = new javax.swing.GroupLayout(editar);
        editar.setLayout(editarLayout);
        editarLayout.setHorizontalGroup(
            editarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(editarLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(editarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16)
                    .addComponent(jLabel8)
                    .addGroup(editarLayout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(numQuestionarios, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(editarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel10)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        editarLayout.setVerticalGroup(
            editarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(editarLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(editarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(numQuestionarios))
                .addGap(4, 4, 4)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5))
        );

        acoes.add(editar, "card3");

        cadastrarData.setDateFormatString("dd/MM/yyyy");

        javax.swing.GroupLayout cadastrarLayout = new javax.swing.GroupLayout(cadastrar);
        cadastrar.setLayout(cadastrarLayout);
        cadastrarLayout.setHorizontalGroup(
            cadastrarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cadastrarLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(cadastrarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(cadastrarLayout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(acoes, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(cadastrarLayout.createSequentialGroup()
                        .addComponent(cadastrarNome)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cadastrarData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        cadastrarLayout.setVerticalGroup(
            cadastrarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cadastrarLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(cadastrarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cadastrarNome)
                    .addComponent(cadastrarData, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(cadastrarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(acoes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        abas.add(cadastrar, "card3");

        pesquisa.setLayout(new java.awt.CardLayout());

        numero.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.background"));
        numero.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        numero.setForeground(new java.awt.Color(102, 102, 102));
        numero.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Número", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(102, 102, 102))); // NOI18N

        bairro.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.background"));
        bairro.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        bairro.setForeground(new java.awt.Color(102, 102, 102));
        bairro.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Bairro", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(102, 102, 102))); // NOI18N

        rua.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.background"));
        rua.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        rua.setForeground(new java.awt.Color(102, 102, 102));
        rua.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Rua", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(102, 102, 102))); // NOI18N

        area.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.background"));
        area.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        area.setForeground(new java.awt.Color(102, 102, 102));
        area.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Área", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(102, 102, 102))); // NOI18N

        javax.swing.GroupLayout enderecoLayout = new javax.swing.GroupLayout(endereco);
        endereco.setLayout(enderecoLayout);
        enderecoLayout.setHorizontalGroup(
            enderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, enderecoLayout.createSequentialGroup()
                .addComponent(bairro, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rua, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(numero, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(area, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        enderecoLayout.setVerticalGroup(
            enderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, enderecoLayout.createSequentialGroup()
                .addGroup(enderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(numero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bairro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rua, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(area, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pesquisa.add(endereco, "card2");

        jLabel22.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(102, 102, 102));
        jLabel22.setText("até");

        data1.setDateFormatString("dd/MM/yyyy");

        data2.setDateFormatString("dd/MM/yyyy");

        javax.swing.GroupLayout dataLayout = new javax.swing.GroupLayout(data);
        data.setLayout(dataLayout);
        dataLayout.setHorizontalGroup(
            dataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dataLayout.createSequentialGroup()
                .addContainerGap(174, Short.MAX_VALUE)
                .addComponent(data1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel22)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(data2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(130, 130, 130))
        );
        dataLayout.setVerticalGroup(
            dataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dataLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(dataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(data2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(data1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22))
                .addContainerGap())
        );

        pesquisa.add(data, "card4");

        especie.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.background"));
        especie.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        especie.setForeground(new java.awt.Color(102, 102, 102));
        especie.setBorder(javax.swing.BorderFactory.createTitledBorder("Espécie"));

        castrado.setText("Castrado");

        masc.setText("Masculino");

        fem.setText("Feminino");

        idade.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.background"));
        idade.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        idade.setForeground(new java.awt.Color(102, 102, 102));
        idade.setBorder(javax.swing.BorderFactory.createTitledBorder("Idade"));

        javax.swing.GroupLayout animalLayout = new javax.swing.GroupLayout(animal);
        animal.setLayout(animalLayout);
        animalLayout.setHorizontalGroup(
            animalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(animalLayout.createSequentialGroup()
                .addComponent(especie, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(castrado)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(masc)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(fem)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(idade, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        animalLayout.setVerticalGroup(
            animalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, animalLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(animalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(castrado)
                    .addComponent(masc)
                    .addComponent(fem))
                .addContainerGap())
            .addGroup(animalLayout.createSequentialGroup()
                .addGroup(animalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(especie, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(idade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pesquisa.add(animal, "card5");

        cisterna.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        cisterna.setForeground(new java.awt.Color(102, 102, 102));
        cisterna.setText("Cisterna");
        cisterna.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cisternaMouseClicked(evt);
            }
        });

        cxdagua.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        cxdagua.setForeground(new java.awt.Color(102, 102, 102));
        cxdagua.setText("Caixa d'água");
        cxdagua.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cxdaguaMouseClicked(evt);
            }
        });

        animais.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        animais.setForeground(new java.awt.Color(102, 102, 102));
        animais.setText("Animais");
        animais.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                animaisMouseClicked(evt);
            }
        });

        fossa.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        fossa.setForeground(new java.awt.Color(102, 102, 102));
        fossa.setText("Fossa sep.");
        fossa.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                fossaItemStateChanged(evt);
            }
        });
        fossa.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                fossaMouseClicked(evt);
            }
        });

        poco.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        poco.setForeground(new java.awt.Color(102, 102, 102));
        poco.setText("Poço art.");
        poco.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pocoMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout questionarioLayout = new javax.swing.GroupLayout(questionario);
        questionario.setLayout(questionarioLayout);
        questionarioLayout.setHorizontalGroup(
            questionarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(questionarioLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cisterna)
                .addGap(31, 31, 31)
                .addComponent(cxdagua)
                .addGap(27, 27, 27)
                .addComponent(poco)
                .addGap(35, 35, 35)
                .addComponent(fossa)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                .addComponent(animais)
                .addContainerGap())
        );
        questionarioLayout.setVerticalGroup(
            questionarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(questionarioLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(questionarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cisterna)
                    .addComponent(cxdagua)
                    .addComponent(animais)
                    .addComponent(fossa)
                    .addComponent(poco))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pesquisa.add(questionario, "card6");

        jLabel3.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(102, 102, 102));
        jLabel3.setText("Pesquisar por:");

        pesquisarPor.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        pesquisarPor.setForeground(new java.awt.Color(102, 102, 102));
        pesquisarPor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Endereço", "Data", "Animal", "Questionário" }));
        pesquisarPor.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                pesquisarPorItemStateChanged(evt);
            }
        });

        jButton5.setForeground(new java.awt.Color(102, 102, 102));
        jButton5.setText("Pesquisar");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jTableResultados.setForeground(new java.awt.Color(51, 51, 51));
        jTableResultados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nome", "Rua", "Número", "Bairro", "Questionários", "ID"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableResultados.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jTableResultadosPropertyChange(evt);
            }
        });
        jTableResultados.addVetoableChangeListener(new java.beans.VetoableChangeListener() {
            public void vetoableChange(java.beans.PropertyChangeEvent evt)throws java.beans.PropertyVetoException {
                jTableResultadosVetoableChange(evt);
            }
        });
        jScrollPane2.setViewportView(jTableResultados);
        if (jTableResultados.getColumnModel().getColumnCount() > 0) {
            jTableResultados.getColumnModel().getColumn(5).setMinWidth(0);
            jTableResultados.getColumnModel().getColumn(5).setPreferredWidth(0);
            jTableResultados.getColumnModel().getColumn(5).setMaxWidth(0);
        }

        jButton6.setForeground(new java.awt.Color(51, 51, 51));
        jButton6.setText("Visualizar");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jLabelNResultados.setForeground(new java.awt.Color(102, 102, 102));
        jLabelNResultados.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelNResultados.setText("0");

        jLabel21.setForeground(new java.awt.Color(102, 102, 102));
        jLabel21.setText("resultados encontrados");

        jButton8.setForeground(new java.awt.Color(102, 102, 102));
        jButton8.setText("Limpar filtros");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton12.setBackground(new java.awt.Color(255, 51, 51));
        jButton12.setForeground(new java.awt.Color(51, 51, 51));
        jButton12.setText("Deletar Pessoa");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout buscarLayout = new javax.swing.GroupLayout(buscar);
        buscar.setLayout(buscarLayout);
        buscarLayout.setHorizontalGroup(
            buscarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, buscarLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(buscarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSeparator2)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pesquisa, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, buscarLayout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pesquisarPor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(buscarLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(buscarLayout.createSequentialGroup()
                        .addComponent(jLabelNResultados, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel21)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        buscarLayout.setVerticalGroup(
            buscarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(buscarLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(buscarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(pesquisarPor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(buscarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton5)
                    .addComponent(jButton8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(buscarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, buscarLayout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 415, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(buscarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton6)
                            .addComponent(jButton12)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, buscarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabelNResultados)
                        .addComponent(jLabel21)))
                .addContainerGap())
        );

        abas.add(buscar, "card4");

        getContentPane().add(abas);
        abas.setBounds(260, 30, 540, 570);

        jPanel6.setBackground(new java.awt.Color(34, 44, 50));
        jPanel6.setPreferredSize(new java.awt.Dimension(500, 79));
        jPanel6.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jPanel6MouseDragged(evt);
            }
        });
        jPanel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanel6MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jPanel6MouseReleased(evt);
            }
        });

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/fechar.png"))); // NOI18N
        jLabel9.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel9MouseClicked(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(240, 240, 240));
        jLabel12.setText("Sistema de Gerenciamento de Formulários");

        jLabel14.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(240, 240, 240));
        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/ajuda.png"))); // NOI18N
        jLabel14.setText("Central de Ajuda");
        jLabel14.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel14.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel14MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(262, Short.MAX_VALUE)
                .addComponent(jLabel14)
                .addGap(199, 199, 199)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel6);
        jPanel6.setBounds(0, 0, 800, 30);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void cadastrarNomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cadastrarNomeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cadastrarNomeActionPerformed

    private void jLabel9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel9MouseClicked
        this.dispose();
    }//GEN-LAST:event_jLabel9MouseClicked

    private void btnHomeFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_btnHomeFocusGained
        // TODO add your handling code here:

    }//GEN-LAST:event_btnHomeFocusGained

    private void btnHomeMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHomeMouseEntered
        // TODO add your handling code here:
        if (!home.isShowing()) {
            btnHome.setForeground(Color.gray);
        }

    }//GEN-LAST:event_btnHomeMouseEntered

    private void btnCadastrarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCadastrarMouseEntered
        // TODO add your handling code here:
        if (!cadastrar.isShowing()) {
            btnCadastrar.setForeground(Color.gray);
        }

    }//GEN-LAST:event_btnCadastrarMouseEntered

    private void btnBuscarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBuscarMouseEntered
        // TODO add your handling code here:
        if (!buscar.isShowing())
            btnBuscar.setForeground(Color.gray);
    }//GEN-LAST:event_btnBuscarMouseEntered

    private void btnHomeMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHomeMouseExited
        // TODO add your handling code here:
        if (!home.isShowing())
            btnHome.setForeground(home.getBackground());
    }//GEN-LAST:event_btnHomeMouseExited

    private void btnCadastrarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCadastrarMouseExited
        // TODO add your handling code here:
        if (!cadastrar.isShowing())
            btnCadastrar.setForeground(home.getBackground());
    }//GEN-LAST:event_btnCadastrarMouseExited

    private void btnBuscarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBuscarMouseExited
        // TODO add your handling code here:
        if (!buscar.isShowing())
            btnBuscar.setForeground(home.getBackground());
    }//GEN-LAST:event_btnBuscarMouseExited

    private void btnHomeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHomeMouseClicked
        // TODO add your handling code here:
        trocarAba(home);
        btnCadastrar.setForeground(cadastrar.getBackground());
        btnBuscar.setForeground(cadastrar.getBackground());

        btnHome.setForeground(divisor.getBackground());

        ImageIcon home = new ImageIcon(getClass().getResource("/imagens/home-verde.png"));
        btnHome.setIcon(home);
        ImageIcon cadastrar = new ImageIcon(getClass().getResource("/imagens/cadastrar.png"));
        btnCadastrar.setIcon(cadastrar);
        ImageIcon buscar = new ImageIcon(getClass().getResource("/imagens/editar.png"));
        btnBuscar.setIcon(buscar);
        atualizarEstatisticas();
    }//GEN-LAST:event_btnHomeMouseClicked

    private void btnCadastrarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCadastrarMouseClicked
        // TODO add your handling code here:
        if (!cadastrar.isShowing() || editar.isShowing()) {
            limparCampos();
            trocarAba(cadastrar);
            trocarCriarEditar(criar);
            btnBuscar.setForeground(cadastrar.getBackground());
            btnHome.setForeground(cadastrar.getBackground());
            btnCadastrar.setForeground(divisor.getBackground());
            ImageIcon home = new ImageIcon(getClass().getResource("/imagens/home.png"));
            btnHome.setIcon(home);
            ImageIcon cadastrar = new ImageIcon(getClass().getResource("/imagens/cadastrar-verde.png"));
            btnCadastrar.setIcon(cadastrar);
            ImageIcon buscar = new ImageIcon(getClass().getResource("/imagens/editar.png"));
            btnBuscar.setIcon(buscar);
        }
    }//GEN-LAST:event_btnCadastrarMouseClicked

    private void btnBuscarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBuscarMouseClicked
        // TODO add your handling code here:
        trocarAba(buscar);
        btnCadastrar.setForeground(cadastrar.getBackground());
        btnHome.setForeground(cadastrar.getBackground());

        btnBuscar.setForeground(divisor.getBackground());

        ImageIcon home = new ImageIcon(getClass().getResource("/imagens/home.png"));
        btnHome.setIcon(home);
        ImageIcon cadastrar = new ImageIcon(getClass().getResource("/imagens/cadastrar.png"));
        btnCadastrar.setIcon(cadastrar);
        ImageIcon buscar = new ImageIcon(getClass().getResource("/imagens/editar-verde.png"));
        btnBuscar.setIcon(buscar);

        limparFiltros();
        pdao.pesquisar(resultadoPesquisa, rua, bairro, numero, data1, data2, especie, cisterna, cxdagua, poco, fossa, animais, area, castrado, masc, fem, idade);
    }//GEN-LAST:event_btnBuscarMouseClicked

    private void cadastrarNumeroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cadastrarNumeroActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cadastrarNumeroActionPerformed

    private void pergunta5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pergunta5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pergunta5ActionPerformed

    private void pergunta4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pergunta4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pergunta4ActionPerformed

    private void pergunta3p1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pergunta3p1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pergunta3p1ActionPerformed

    private void pergunta3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pergunta3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pergunta3ActionPerformed

    private void pergunta2p1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pergunta2p1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pergunta2p1ActionPerformed

    private void pergunta1p2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pergunta1p2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pergunta1p2ActionPerformed

    private void pergunta2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pergunta2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pergunta2ActionPerformed

    private void pergunta1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pergunta1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pergunta1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        limparCampos();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jLabel14MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel14MouseClicked
        // TODO add your handling code here:
        try {
            URI link = new URI("https://docs.google.com/document/d/1CTSn__7z0pUHFGJyYS8ozKhGgTSCDaFQb0cC9rKS3Mk/edit?usp=sharing");
            Desktop.getDesktop().browse(link);
        } catch (Exception erro) {
            System.out.println(erro);
        }
    }//GEN-LAST:event_jLabel14MouseClicked

    private void pesquisarPorItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_pesquisarPorItemStateChanged
        // TODO add your handling code here:
        if (pesquisarPor.getSelectedItem().equals("Endereço")) {
            trocarPesquisa(endereco);
        }
        if (pesquisarPor.getSelectedItem().equals("Data")) {
            trocarPesquisa(data);
        }
        if (pesquisarPor.getSelectedItem().equals("Animal")) {
            trocarPesquisa(animal);
        }
        if (pesquisarPor.getSelectedItem().equals("Questionário")) {
            trocarPesquisa(questionario);
        }
    }//GEN-LAST:event_pesquisarPorItemStateChanged

    private void btnImportarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnImportarMouseEntered
        // TODO add your handling code here:
        btnImportar.setForeground(Color.gray);
    }//GEN-LAST:event_btnImportarMouseEntered

    private void btnImportarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnImportarMouseExited
        // TODO add your handling code here:
        btnImportar.setForeground(cadastrar.getBackground());
    }//GEN-LAST:event_btnImportarMouseExited

    private void btnExportarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnExportarMouseEntered
        // TODO add your handling code here:
        btnExportar.setForeground(Color.gray);
    }//GEN-LAST:event_btnExportarMouseEntered

    private void btnExportarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnExportarMouseExited
        // TODO add your handling code here:
        btnExportar.setForeground(cadastrar.getBackground());
    }//GEN-LAST:event_btnExportarMouseExited

    private void cisternaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cisternaMouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_cisternaMouseClicked

    private void cxdaguaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cxdaguaMouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_cxdaguaMouseClicked

    private void pocoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pocoMouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_pocoMouseClicked

    private void fossaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fossaMouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_fossaMouseClicked

    private void animaisMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_animaisMouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_animaisMouseClicked

    private void fossaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_fossaItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_fossaItemStateChanged

    private void pergunta1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pergunta1MouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_pergunta1MouseClicked

    private void pergunta2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pergunta2MouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_pergunta2MouseClicked

    private void pergunta3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pergunta3MouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_pergunta3MouseClicked

    private void pergunta5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pergunta5MouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_pergunta5MouseClicked

    private void jPanel6MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel6MouseDragged
        // TODO add your handling code here:
        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();

        this.setLocation(x - xMouse, y - yMouse);
    }//GEN-LAST:event_jPanel6MouseDragged

    private void jPanel6MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel6MouseReleased
        // TODO add your handling code here:
        setOpacity((float) 1.0);
    }//GEN-LAST:event_jPanel6MouseReleased

    private void jPanel6MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel6MousePressed
        // TODO add your handling code here:
        setOpacity((float) 0.8);
        xMouse = evt.getX();
        yMouse = evt.getY();
    }//GEN-LAST:event_jPanel6MousePressed

    private void jComboBoxBairrosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jComboBoxBairrosMouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_jComboBoxBairrosMouseClicked

    private void jComboBoxRuasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jComboBoxRuasMouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_jComboBoxRuasMouseClicked

    private void jComboBoxBairrosItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxBairrosItemStateChanged
        // TODO add your handling code here:
        enddao.getRuas(jComboBoxBairros, jComboBoxRuas);
    }//GEN-LAST:event_jComboBoxBairrosItemStateChanged

    private void jComboBoxRuasMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jComboBoxRuasMouseEntered
        // TODO add your handling code here:

    }//GEN-LAST:event_jComboBoxRuasMouseEntered

    private void jTableResultadosPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jTableResultadosPropertyChange
        // TODO add your handling code here:

    }//GEN-LAST:event_jTableResultadosPropertyChange

    private void jTableResultadosVetoableChange(java.beans.PropertyChangeEvent evt)throws java.beans.PropertyVetoException {//GEN-FIRST:event_jTableResultadosVetoableChange
        // TODO add your handling code here:

    }//GEN-LAST:event_jTableResultadosVetoableChange

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        pdao.pesquisar(resultadoPesquisa, rua, bairro, numero, data1, data2, especie, cisterna, cxdagua, poco, fossa, animais, area, castrado, masc, fem, idade);
        jLabelNResultados.setText(resultadoPesquisa.getRowCount() + "");
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
        limparFiltros();
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        if (jTableResultados.getSelectedRow() >= 0) {
            idpessoa = Integer.parseInt(jTableResultados.getValueAt(jTableResultados.getSelectedRow(), 5).toString());
            limparCampos();
            preencherFormulario(idpessoa);
            trocarAba(cadastrar);
            trocarCriarEditar(editar);
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jTableQuestionariosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableQuestionariosMouseClicked
        // TODO add your handling code here:
        preencherFormulario(idpessoa);
    }//GEN-LAST:event_jTableQuestionariosMouseClicked

    private void jTableAnimaisMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableAnimaisMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTableAnimaisMouseClicked

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        listaAnimais.addRow(new Object[]{"", ""});
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
        try {
            adao.deletarAnimal(Integer.parseInt(jTableAnimais.getValueAt(jTableAnimais.getSelectedRow(), 5).toString()));
            listaAnimais.removeRow(jTableAnimais.getSelectedRow());

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Você precisa selecionar uma linha correspondente a uma espécie de animal para remover.");
        }
    }//GEN-LAST:event_jButton9ActionPerformed

    private void cadastrarComplementoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cadastrarComplementoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cadastrarComplementoActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // TODO add your handling code here:
        trocarCampoRua(selecaoRua);
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jComboBoxRuasItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxRuasItemStateChanged
        // TODO add your handling code here:

    }//GEN-LAST:event_jComboBoxRuasItemStateChanged

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        if (cadastrarData.getDate() != null) {
            int id = pdao.cadastrarPessoa(cadastrarRuaCampo, cadastrarArea, campoRua, cadastrarComplemento, cadastrarNome, jComboBoxRuas, jComboBoxBairros, cadastrarNumero);

            if (qdao.cadastrarQuestionario(id, pergunta1, pergunta1p2, pergunta2, pergunta2p1, pergunta2p2, pergunta3, pergunta3p1, pergunta4, pergunta5, cadastrarData, this) && adao.cadastrarAnimal(id, jTableAnimais, cadastrarData) && id != 0) {
                limparCampos();
                JOptionPane.showMessageDialog(this, "Questionário salvo com sucesso!");
            }

        } else {
            JOptionPane.showMessageDialog(this, "Insira uma data válida.");
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        // TODO add your handling code here:
        trocarCampoRua(campoRua);
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        String mensagem = "";
        if (pdao.editarPessoa(idpessoa, cadastrarRuaCampo, cadastrarArea, campoRua, cadastrarComplemento, cadastrarNome, jComboBoxRuas, jComboBoxBairros, cadastrarNumero)) {
            mensagem += "pessoa ";
        }

        if (jTableQuestionarios.getSelectedRow() >= 0) {
            String data = listaQuestionarios.getValueAt(jTableQuestionarios.getSelectedRow(), 0) + "";
            if (qdao.editarQuestionario(idpessoa, pergunta1, pergunta1p2, pergunta2, pergunta2p1, pergunta2p2, pergunta3, pergunta3p1, pergunta4, pergunta5, cadastrarData, data, this) && adao.editarAnimal(idpessoa, jTableAnimais, cadastrarData, listaAnimais)) {
                mensagem += "e questionário";
            }
        }

        JOptionPane.showMessageDialog(this, "Os seguintes dados foram salvos com sucesso: " + mensagem);

        listaQuestionarios.setRowCount(0);
        for (Questionario quests : pdao.getPessoa(idpessoa).getQuestionario()) {
            listaQuestionarios.addRow(new Object[]{quests.getData()});
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        // TODO add your handling code here:
        try {
            if (pdao.deletarPessoa(Integer.parseInt(jTableResultados.getValueAt(jTableResultados.getSelectedRow(), 5).toString()))) {
                JOptionPane.showMessageDialog(this, "Pessoa deletada com sucesso!");
                pdao.pesquisar(listaAnimais, rua, bairro, numero, data1, data2, especie, cisterna, cxdagua, poco, fossa, animais, area, castrado, masc, fem, idade);
                limparFiltros();
                pdao.pesquisar(resultadoPesquisa, rua, bairro, numero, data1, data2, especie, cisterna, cxdagua, poco, fossa, animais, area, castrado, masc, fem, idade);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Selecione uma pessoa na tabela para deletar.");
        }
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        qdao.deletarQuestionario(idpessoa, cadastrarData);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void btnExportarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnExportarMouseClicked
        // TODO add your handling code here:
        if(buscar.isShowing()){
        if(exportarCSV()==1)
            JOptionPane.showMessageDialog(this, "Tabela de resultados exportada com sucesso!");
        else if(exportarCSV()==0)
            JOptionPane.showMessageDialog(this, "Ocorreu um erro ao exportar os dados. Contate o desenvolvedor.");
        } else {
            JOptionPane.showMessageDialog(this, "Navegue até a aba buscar para selecionar o que deseja exportar.");
        }
    }//GEN-LAST:event_btnExportarMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Interface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Interface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Interface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Interface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Interface().setVisible(true);
                //new Erro().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel abas;
    private javax.swing.JPanel acoes;
    private javax.swing.JCheckBox animais;
    private javax.swing.JPanel animal;
    private javax.swing.JTextField area;
    private javax.swing.JTextField bairro;
    private javax.swing.JLabel btnBuscar;
    private javax.swing.JLabel btnCadastrar;
    private javax.swing.JLabel btnExportar;
    private javax.swing.JLabel btnHome;
    private javax.swing.JLabel btnImportar;
    private javax.swing.JPanel buscar;
    private javax.swing.JPanel cadastrar;
    private javax.swing.JComboBox<String> cadastrarArea;
    private javax.swing.JTextField cadastrarComplemento;
    private com.toedter.calendar.JDateChooser cadastrarData;
    private javax.swing.JTextField cadastrarNome;
    private javax.swing.JTextField cadastrarNumero;
    private javax.swing.JTextField cadastrarRuaCampo;
    private javax.swing.JPanel campoRua;
    private javax.swing.JCheckBox castrado;
    private javax.swing.JCheckBox cisterna;
    private javax.swing.JPanel criar;
    private javax.swing.JCheckBox cxdagua;
    private javax.swing.JPanel data;
    private com.toedter.calendar.JDateChooser data1;
    private com.toedter.calendar.JDateChooser data2;
    private javax.swing.JPanel divisor;
    private javax.swing.JPanel editar;
    private javax.swing.JPanel endereco;
    private javax.swing.JTextField especie;
    private javax.swing.JCheckBox fem;
    private javax.swing.JCheckBox fossa;
    private javax.swing.JPanel home;
    private javax.swing.JTextField idade;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox<String> jComboBoxBairros;
    private javax.swing.JComboBox<String> jComboBoxRuas;
    private javax.swing.JComboBox<String> jComboBoxSexoAnimal;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelNResultados;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel jPanelRua;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTable jTableAnimais;
    private javax.swing.JTable jTableQuestionarios;
    private javax.swing.JTable jTableResultados;
    private javax.swing.JCheckBox masc;
    private javax.swing.JLabel numQuestionarios;
    private javax.swing.JTextField numero;
    private javax.swing.JLabel p1p2;
    private javax.swing.JLabel p2p1;
    private javax.swing.JLabel p2p2;
    private javax.swing.JLabel p3p1;
    private javax.swing.JLabel p5p1;
    private javax.swing.JCheckBox pergunta1;
    private javax.swing.JCheckBox pergunta1p2;
    private javax.swing.JCheckBox pergunta2;
    private javax.swing.JCheckBox pergunta2p1;
    private javax.swing.JSpinner pergunta2p2;
    private javax.swing.JCheckBox pergunta3;
    private javax.swing.JCheckBox pergunta3p1;
    private javax.swing.JCheckBox pergunta4;
    private javax.swing.JCheckBox pergunta5;
    private javax.swing.JPanel pesquisa;
    private javax.swing.JComboBox<String> pesquisarPor;
    private javax.swing.JCheckBox poco;
    private javax.swing.JLabel possuemAnimais;
    private javax.swing.JLabel possuemCaixa;
    private javax.swing.JLabel possuemFossa;
    private javax.swing.JLabel possuemPoco;
    private javax.swing.JPanel questionario;
    private javax.swing.JTextField rua;
    private javax.swing.JPanel selecaoRua;
    private javax.swing.JPanel selecaoRua1;
    private javax.swing.JLabel totalForms;
    private javax.swing.JLabel totalPessoas;
    // End of variables declaration//GEN-END:variables
}
