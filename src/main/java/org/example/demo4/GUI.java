package org.example.demo4;

import socketapp.Cekirdek;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

public class GUI {
    private static File fileToChoose = null;
    private static String lastSearchedWord = "";

    public static void GUIPlay() {
        // Swing GUI

        JFrame frame1 = new JFrame("Paralel Dosya");
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame1.setExtendedState(JFrame.MAXIMIZED_BOTH); // Tam ekran modu
        frame1.setLayout(new BorderLayout());

        // Üst panel: arama kelimesi ve dosya seçimi
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(246, 239, 239));
        topPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Arama kelimesi alanı
        gbc.gridx = 1;
        gbc.gridy = 0;
        topPanel.add(new JLabel("Aranacak Kelime:"), gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        JTextArea txtKelime = new JTextArea(1, 20);
        topPanel.add(txtKelime, gbc);
        String placeholderText = "Buraya yazınız...";
        txtKelime.setText(placeholderText);
        txtKelime.setForeground(Color.GRAY);
        txtKelime.setFont(txtKelime.getFont().deriveFont(Font.ITALIC));

        txtKelime.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtKelime.getText().equals(placeholderText)) {
                    txtKelime.setText("");
                    txtKelime.setForeground(Color.BLACK);
                    txtKelime.setFont(txtKelime.getFont().deriveFont(Font.PLAIN));
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (txtKelime.getText().isEmpty()) {
                    txtKelime.setText(placeholderText);
                    txtKelime.setForeground(Color.GRAY);
                    txtKelime.setFont(txtKelime.getFont().deriveFont(Font.ITALIC));
                }
            }
        });
        // Kelime ve karakter sayısı etiketleri
        gbc.gridx = 1;
        gbc.gridy = 2;
        JLabel lblKelime = new JLabel("Kelime sayısı:");
        topPanel.add(lblKelime, gbc);

        gbc.gridx = 2;
        gbc.gridy = 2;
        JLabel lblKarakter = new JLabel("Karakter sayısı:");
        topPanel.add(lblKarakter, gbc);

        // Dosya seçme butonu
        gbc.gridx = 0;
        gbc.gridy = 0;
        JButton btnFile = new JButton();
        try {
            ImageIcon selectButtonIcon = new ImageIcon(Objects.requireNonNull(GUI.class.getResource("/kaynaklar/dosya.png")));
            Image selectButtonImage = selectButtonIcon.getImage();
            Image scaledSelectButtonImage = selectButtonImage.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            btnFile.setIcon(new ImageIcon(scaledSelectButtonImage));
        } catch (Exception e) {
            e.printStackTrace();
        }
        btnFile.setPreferredSize(new Dimension(80, 80));
        topPanel.add(btnFile, gbc);

        // Arama butonu
        gbc.gridx = 7;
        gbc.gridy = 0;
        JButton btnSearch = new JButton();
        try {
            ImageIcon searchButtonIcon = new ImageIcon(Objects.requireNonNull(GUI.class.getResource("/kaynaklar/aramaicon.png"))); // Resmi dosya yolundan yükle
            Image searchButtonImage = searchButtonIcon.getImage();
            Image scaledSearchButtonImage = searchButtonImage.getScaledInstance(70, 70, Image.SCALE_SMOOTH);
            btnSearch.setIcon(new ImageIcon(scaledSearchButtonImage));
        } catch (Exception e) {
            e.printStackTrace();
        }
        btnSearch.setPreferredSize(new Dimension(70, 70));
        topPanel.add(btnSearch, gbc);

        frame1.add(topPanel, BorderLayout.NORTH);

        // Metin alanı ve kaydırma paneli
        JTextArea textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        frame1.add(scrollPane, BorderLayout.CENTER);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JFileChooser fileChooser = new JFileChooser();

        btnFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {

                // Yeni bir JDialog oluştur
                JDialog dialog = new JDialog(frame1, "Dosya İşlemleri", true);
                dialog.setLayout(new GridBagLayout());
                dialog.setSize(300, 150);
                dialog.setLocationRelativeTo(frame1); // Dialogu ortalar

                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(5, 5, 5, 5);

                // Mesaj etiketi
                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.gridwidth = 2;
                dialog.add(new JLabel("Lütfen bir işlem seçiniz:"), gbc);

                // Dosya Seç butonu
                JButton btnChooseFile = new JButton("Dosya Seç");
                btnChooseFile.setBackground(new Color(246, 239, 239));
                btnChooseFile.setForeground(Color.BLACK);
                gbc.gridx = 0;
                gbc.gridy = 1;
                gbc.gridwidth = 1;
                dialog.add(btnChooseFile, gbc);

                // Yeni Dosya Oluştur butonu
                JButton btnDosyaOlustur = new JButton("Yeni Dosya Oluştur");
                btnDosyaOlustur.setBackground(new Color(246, 239, 239));
                btnDosyaOlustur.setForeground(Color.BLACK);
                gbc.gridx = 1;
                gbc.gridy = 1;
                dialog.add(btnDosyaOlustur, gbc);

                // Dosya Seç butonuna tıklama işlemi
                btnChooseFile.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        dialog.dispose(); // Diyaloğu kapat
                        int returnVal = fileChooser.showOpenDialog(null);
                        if (returnVal == JFileChooser.APPROVE_OPTION) { // Kullanıcı dosya seçtiyse devam et
                            File file = fileChooser.getSelectedFile();
                            try {
                                textArea.read(new FileReader(file.getAbsolutePath()), null); // Dosyayı oku
                                textArea.setCaretPosition(0); // Metin alanını en başa getir
                            } catch (IOException ex) {
                                System.out.println("Dosyaya ulaşırken bir hata oluştu: " + file.getAbsolutePath());
                            }
                            fileToChoose = file; // Seçilen dosyayı sakla
                            updateWordAndCharCount(textArea.getText(), lblKelime, lblKarakter);

                        }
                    }
                });


                // Yeni Dosya Oluştur butonuna tıklama işlemi
                btnDosyaOlustur.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        dialog.dispose(); // Diyaloğu kapat
                        Font font = textArea.getFont(); // Mevcut font bilgisini al
                        float fontSize = font.getSize() + 5; // Mevcut font boyutunu 5 birim artır
                        textArea.setFont(font.deriveFont(fontSize));

                        String placeholderText = "Buraya yazınız...";
                        textArea.setText(placeholderText);
                        textArea.setForeground(Color.GRAY);
                        textArea.setFont(textArea.getFont().deriveFont(Font.ITALIC));

                        textArea.addFocusListener(new FocusListener() {
                            @Override
                            public void focusGained(FocusEvent e) {
                                if (textArea.getText().equals(placeholderText)) {
                                    textArea.setText("");
                                    textArea.setForeground(Color.BLACK);
                                    textArea.setFont(textArea.getFont().deriveFont(Font.PLAIN));
                                }
                            }

                            @Override
                            public void focusLost(FocusEvent e) {
                                if (textArea.getText().isEmpty()) {
                                    textArea.setText(placeholderText);
                                    textArea.setForeground(Color.GRAY);
                                    textArea.setFont(textArea.getFont().deriveFont(Font.ITALIC));
                                }
                            }
                        });

                        // Kaydet butonunu oluştur ve topPanel'e ekle
                        gbc.gridx = 8;
                        gbc.gridy = 0;
                        JButton btnSave = new JButton();
                        try {
                            ImageIcon saveButtonIcon = new ImageIcon(Objects.requireNonNull(GUI.class.getResource("/kaynaklar/kaydet (yeni).png")));
                            Image saveButtonImage = saveButtonIcon.getImage();
                            Image scaledSaveButtonImage = saveButtonImage.getScaledInstance(113, 30, Image.SCALE_SMOOTH);
                            btnSave.setIcon(new ImageIcon(scaledSaveButtonImage));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        btnSave.setPreferredSize(new Dimension(113, 30));
                        topPanel.add(btnSave, gbc);

                        // topPanel'i yeniden çiz
                        topPanel.revalidate();
                        topPanel.repaint();

                        // Kaydet butonu işlemi
                        btnSave.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                fileChooser.setDialogTitle("Kaydet");
                                String text = textArea.getText();
                                String[] words = text.split("\\s+");
                                lblKelime.setText("Kelime sayısı: " + words.length);
                                lblKarakter.setText("Karakter sayısı: " + (text.length() - 1));

                                int userSelection = fileChooser.showSaveDialog(frame1);
                                if (userSelection == JFileChooser.APPROVE_OPTION) {
                                    File fileToSave = fileChooser.getSelectedFile();
                                    fileToChoose = fileToSave;
                                    try (FileWriter fileWriter = new FileWriter(fileToSave)) {
                                        fileWriter.write(textArea.getText());
                                        JOptionPane.showMessageDialog(frame1, "Dosya başarıyla kaydedildi: " + fileToSave.getAbsolutePath() + "\n Şimdi kelime arama yapabilirsiniz.");
                                    } catch (IOException ex) {
                                        JOptionPane.showMessageDialog(frame1, "Dosya kaydedilirken bir hata oluştu.", "Hata", JOptionPane.ERROR_MESSAGE);
                                    }
                                }
                            }
                        });
                    }
                });

                dialog.setVisible(true);
            }
        });

        // Arama butonu işlemi
        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (fileToChoose == null) {
                    JOptionPane.showMessageDialog(frame1, "Lütfen önce bir dosya kaydedin.", "Uyarı", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                String kelime = txtKelime.getText();
                if (kelime.isEmpty()) {
                    JOptionPane.showMessageDialog(frame1, "Lütfen aramak istediğiniz kelimeyi girin.", "Uyarı", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                lastSearchedWord = kelime;
                highlightWords(textArea, lastSearchedWord);
                highlightWords(textArea, kelime);
                Cekirdek cekirdek = new Cekirdek(fileToChoose, kelime);
                cekirdek.cekirdeklereBolme();
                int total = cekirdek.getToplamKelimeSayisi();

                UIManager.put("OptionPane.yesButtonText", "Evet");
                UIManager.put("OptionPane.noButtonText", "Hayır");

                Color buttonColor = new Color(246, 239, 239);
                UIManager.put("Button.background", buttonColor);
                UIManager.put("Button.foreground", Color.BLACK); // Yazı rengini istediğiniz gibi ayarlayabilirsiniz

                String[] options = {"Evet", "Hayır"};
                int result = JOptionPane.showOptionDialog(
                        frame1,
                        "'"+kelime +"'"+ " kelimesi metinde " + total + " kere geçmektedir. \nSonucun E-Mail adresinize gönderilmesini ister misiniz?",
                        "Swing Tester",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[0]
                );

                if (result == JOptionPane.YES_OPTION) {
                    org.example.demo4.EmailGonder email = new org.example.demo4.EmailGonder(kelime, total);
                }



            }
        });

        frame1.setVisible(true);
    }
    private static void updateWordAndCharCount(String text, JLabel lblKelime, JLabel lblKarakter) {
        String[] words = text.split("\\s+");
        lblKelime.setText("Kelime sayısı: " + words.length);
        lblKarakter.setText("Karakter sayısı: " + (text.length() - 1));
    }
    private static boolean isTextFile(File file) {
        String[] textFileExtensions = {"txt", "java", "xml", "html", "htm", "csv", "json"};
        String fileName = file.getName();
        for (String extension : textFileExtensions) {
            if (fileName.endsWith("." + extension)) {
                return true;
            }
        }
        return false;
    }

    private static void highlightWords(JTextArea textArea, String word) {
        try {
            Highlighter highlighter = textArea.getHighlighter();
            highlighter.removeAllHighlights(); // Mevcut tüm highlight'ları kaldır

            Highlighter.HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.yellow);
            String text = textArea.getText().toLowerCase(); // Tüm metni küçük harfe dönüştür
            String searchWord = word.toLowerCase(); // Aranan kelimeyi küçük harfe dönüştür
            int pos = 0;

            while ((pos = text.indexOf(searchWord, pos)) >= 0) {
                highlighter.addHighlight(pos, pos + searchWord.length(), painter);
                pos += searchWord.length();
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }





}
