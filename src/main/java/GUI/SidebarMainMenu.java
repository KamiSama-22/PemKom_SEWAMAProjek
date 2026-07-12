/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import GUI.AdminPage;
import GUI.AttendancePage;
import GUI.Panel.MahasiswaPanel;
import GUI.Panel.Settings;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author user
 */
public class SidebarMainMenu extends JPanel implements Services.I18nService.I18nChangeListener {

    // --- TEMA TERANG (LIGHT MODE) ---
    // Background utama menjadi Putih
    private final Color SIDEBAR_BG = new Color(255, 255, 255); 
    private final Color SUBMENU_BG = new Color(255, 255, 255); 
    
    // Header menu diberi warna abu-abu sangat muda agar ada batas visual
    private final Color MENU_BG = new Color(245, 247, 250);
    
    // Warna Hover & Active menggunakan Cyan (menyatu dengan header aplikasi)
    private final Color HOVER_BG = new Color(0, 188, 212); // Cyan agak terang
    private final Color ACTIVE_BG = new Color(0, 151, 167); // Cyan gelap
    
    // Dinamisasi Warna Teks
    private final Color TEXT_NORMAL = new Color(51, 51, 51); // Abu-abu gelap agar terbaca di background putih
    private final Color TEXT_ACTIVE = Color.WHITE; // Putih untuk tombol yang disorot/aktif

    private JButton activeButton = null;

    public SidebarMainMenu() {
        this.setPreferredSize(new Dimension(260, 0));
        this.setBackground(SIDEBAR_BG); 
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // DASHBOARD SECTION
        this.add(createAccordion(
                "ui.sidebar.masterdata",
                new String[]{"ui.sidebar.employee", "ui.sidebar.log", "ui.sidebar.user"}
        ));

        // MANAGEMENT SECTION
        this.add(createAccordion(
                "ui.sidebar.attendance",
                new String[]{"ui.sidebar.kiosk", "ui.sidebar.history", "ui.sidebar.analytics"}
        ));

        // SETTINGS SECTION
        this.add(createAccordion(
                "ui.sidebar.settings",
                new String[]{"ui.sidebar.general"}
        ));

        // REPORT SECTION
        this.add(createAccordion(
                "ui.sidebar.report",
                new String[]{"ui.sidebar.reportlog", "ui.sidebar.performance"}
        ));

        this.add(Box.createVerticalGlue());
        
        Services.I18nService.registerListener(this);
    }

    private JPanel createAccordion(String title, String[] menus) {

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(SIDEBAR_BG); 

        // HEADER BUTTON
        JButton header = new JButton(Services.I18nService.get(title));
        header.putClientProperty("i18nKey", title);

        header.setFocusPainted(false);
        header.setBackground(MENU_BG);
        header.setForeground(TEXT_NORMAL); // Teks header gelap
        header.setBorder(new EmptyBorder(15, 15, 15, 15));
        header.setHorizontalAlignment(SwingConstants.LEFT);
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        // BODY PANEL
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(SIDEBAR_BG);

        for (String menu : menus) {

            JButton btn = new JButton(Services.I18nService.get(menu));
            btn.putClientProperty("i18nKey", menu);

            btn.setFocusPainted(false);
            btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
            btn.setBackground(SUBMENU_BG);
            btn.setForeground(TEXT_NORMAL); // Teks submenu default gelap
            btn.setBorder(new EmptyBorder(10, 20, 10, 10));
            btn.setHorizontalAlignment(SwingConstants.LEFT);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

            // HOVER
            btn.addMouseListener(new java.awt.event.MouseAdapter() {

                @Override
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    if (btn != activeButton) {
                        btn.setBackground(HOVER_BG);
                        btn.setForeground(TEXT_ACTIVE); // Teks berubah putih saat disorot
                    }
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    if (btn != activeButton) {
                        btn.setBackground(SUBMENU_BG);
                        btn.setForeground(TEXT_NORMAL); // Teks kembali gelap saat mouse pergi
                    }
                }
            });

            // ACTION
            btn.addActionListener(e -> {

                switch (menu) {
                    case "ui.sidebar.employee":
                        showPage(new MahasiswaPanel());
                        break;
                    case "ui.sidebar.log":
                        showPage(null);
                        break;
                    case "ui.sidebar.user":
                        showPage(null);
                        break;
                    case "ui.sidebar.kiosk":
                        showPage(new AttendancePage());
                        JFrame kioFrame = (JFrame) SwingUtilities.getWindowAncestor(SidebarMainMenu.this);
                        if (kioFrame != null) {
                            kioFrame.dispose();
                        }
                        break;
                    case "ui.sidebar.history":
                        showPage(null);
                        break;
                    case "ui.sidebar.analytics":
                        showPage(null);
                        break;
                    case "ui.sidebar.general":
                        showPage(new Settings());
                        break;
                    default:
                        showPage(null);
                        break;
                }

                // RESET OLD ACTIVE BUTTON
                if (activeButton != null) {
                    activeButton.setBackground(SUBMENU_BG);
                    activeButton.setForeground(TEXT_NORMAL); // Kembalikan teks tombol lama jadi gelap
                }

                // SET NEW ACTIVE BUTTON
                activeButton = btn;
                btn.setBackground(ACTIVE_BG);
                btn.setForeground(TEXT_ACTIVE); // Ubah teks tombol aktif jadi putih
                
            });

            body.add(btn);
        }

        // DEFAULT COLLAPSE
        body.setVisible(false);

        header.addActionListener(e -> {
            body.setVisible(!body.isVisible());
            container.revalidate();
            container.repaint();
        });

        container.add(header);
        container.add(body);

        return container;
    }

    private void showPage(Component comp) {
        switch (comp) {
        case JPanel pnl -> {
                AdminPage.appContentPane.removeAll();
                AdminPage.appContentPane.add(pnl, BorderLayout.CENTER);
                AdminPage.appContentPane.revalidate();
                AdminPage.appContentPane.repaint();
            }
            case JFrame frm -> {
                JFrame mainFrame = (JFrame) SwingUtilities.getWindowAncestor(SidebarMainMenu.this);
                if (mainFrame != null) {
                    mainFrame.dispose();
                }
                frm.setExtendedState(Frame.MAXIMIZED_BOTH);
                frm.setVisible(true);
            }
            default -> {
            }
        }
    }
      
    @Override
    public void onLanguageChanged() {
        updateComponentText(this);
        this.revalidate();
        this.repaint();
    }

    private void updateComponentText(Component comp) {
        if (comp instanceof JButton) {
            JButton btn = (JButton) comp;
            String key = (String) btn.getClientProperty("i18nKey");
            if (key != null) {
                btn.setText(Services.I18nService.get(key));
            }
        }
        if (comp instanceof java.awt.Container) {
            for (Component c : ((java.awt.Container)comp).getComponents()) {
                updateComponentText(c);
            }
        }
    }
}