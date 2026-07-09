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

    private final Color SIDEBAR_BG = new Color(30, 41, 59);
    private final Color MENU_BG = new Color(51, 65, 85);
    private final Color SUBMENU_BG = new Color(15, 23, 42);
    private final Color HOVER_BG = new Color(37, 99, 235);
    private final Color ACTIVE_BG = new Color(59, 130, 246);
    private final Color TEXT_COLOR = Color.WHITE;

    private JButton activeButton = null;

    public SidebarMainMenu() {
        this.setPreferredSize(new Dimension(260, 0));
        this.setBackground(new Color(33, 37, 41));
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
                new String[]{"ui.sidebar.general", "ui.settings.tab1"}
        ));

        // REPORT SECTION
        this.add(createAccordion(
                "ui.sidebar.report",
                new String[]{"ui.sidebar.reportlog", "ui.sidebar.performance"}
        ));

        this.add(Box.createVerticalGlue());
        Services.I18nService.registerListener(this);
    }

    @Override
    public void onLanguageChanged() {
        updateComponentText(this);
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

    private JPanel createAccordion(String title, String[] menus) {

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(new Color(33, 37, 41));

        // HEADER BUTTON
        JButton header = new JButton(Services.I18nService.get(title));
        header.putClientProperty("i18nKey", title);

        header.setFocusPainted(false);
        header.setBackground(MENU_BG);
        header.setForeground(TEXT_COLOR);
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
            btn.setForeground(TEXT_COLOR);
            btn.setBorder(new EmptyBorder(10, 20, 10, 10));
            btn.setHorizontalAlignment(SwingConstants.LEFT);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

            // HOVER
            btn.addMouseListener(new java.awt.event.MouseAdapter() {

                @Override
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    btn.setBackground(HOVER_BG);
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    btn.setBackground(SUBMENU_BG);
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
                        // Untuk KiosK, tutup AdminPage karena buka JFrame baru
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

                    case "ui.settings.tab1":
                        showPage(null);
                        break;
                        
                    default:
                        showPage(null);
                        break;
                }

                // RESET OLD ACTIVE BUTTON
                if (activeButton != null) {
                    activeButton.setBackground(SUBMENU_BG);
                }

                // SET NEW ACTIVE BUTTON
                activeButton = btn;
                btn.setBackground(ACTIVE_BG);
                
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
        if (comp instanceof JPanel pnl) {
            AdminPage.appContentPane.removeAll();
            AdminPage.appContentPane.add(pnl, BorderLayout.CENTER);

            AdminPage.appContentPane.revalidate();
            AdminPage.appContentPane.repaint();
        }else if (comp instanceof JFrame frm) {
            frm.setExtendedState(Frame.MAXIMIZED_BOTH); 
            frm.setVisible(true); 
        }
    }

}
