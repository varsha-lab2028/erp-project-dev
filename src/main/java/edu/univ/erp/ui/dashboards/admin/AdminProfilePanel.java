package edu.univ.erp.ui.dashboards.admin;

import edu.univ.erp.ui.common.DashboardComponents;
import edu.univ.erp.ui.common.DashboardTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AdminProfilePanel extends JPanel {

    private CardLayout cardLayout;
    private JPanel contentContainer;

    public AdminProfilePanel() {
        setLayout(new BorderLayout());
        setBackground(DashboardTheme.BG_MAIN);
        setBorder(new EmptyBorder(30, 30, 30, 30));

        cardLayout = new CardLayout();
        contentContainer = new JPanel(cardLayout);
        contentContainer.setBackground(DashboardTheme.BG_MAIN);

        
        contentContainer.add(createViewPanel(), "VIEW");
        
        
        contentContainer.add(new EditProfilePanel(
            () -> showView(),   
            () -> showView()    
        ), "EDIT");

        add(contentContainer, BorderLayout.CENTER);
    }

    private void showView() {
        cardLayout.show(contentContainer, "VIEW");
    }

    private void showEdit() {
        cardLayout.show(contentContainer, "EDIT");
    }

   
    private JPanel createViewPanel() {
        JPanel viewPanel = new JPanel();
        viewPanel.setLayout(new BoxLayout(viewPanel, BoxLayout.Y_AXIS));
        viewPanel.setBackground(DashboardTheme.BG_MAIN);

      
        DashboardComponents.CardPanel headerCard = new DashboardComponents.CardPanel();
        headerCard.setLayout(new BorderLayout());
        
        JPanel profileContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 20));
        profileContainer.setOpaque(false);

        profileContainer.add(new DashboardComponents.CircleAvatar("AD", 100));

        JPanel textInfo = new JPanel(new GridLayout(3, 1, 0, 5));
        textInfo.setOpaque(false);
        
        JLabel name = new JLabel("System Administrator");
        name.setFont(new Font("Segoe UI", Font.BOLD, 26));
        name.setForeground(DashboardTheme.TEXT_PRIMARY);
        
        JLabel role = new JLabel("IT Department • IIITD");
        role.setFont(DashboardTheme.FONT_REGULAR);
        role.setForeground(DashboardTheme.TEXT_SECONDARY);
        
        JLabel status = new JLabel(" ● Online");
        status.setFont(DashboardTheme.FONT_SMALL);
        status.setForeground(DashboardTheme.SUCCESS);

        textInfo.add(name);
        textInfo.add(role);
        textInfo.add(status);
        
        profileContainer.add(textInfo);
        headerCard.add(profileContainer, BorderLayout.CENTER);
        
        
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setOpaque(false);
        btnPanel.setBorder(new EmptyBorder(30,0,0,30));
        
        JButton editBtn = DashboardComponents.createPrimaryButton("Edit Profile");
        editBtn.addActionListener(e -> showEdit()); 
        
        btnPanel.add(editBtn);
        headerCard.add(btnPanel, BorderLayout.EAST);

        viewPanel.add(headerCard);
        viewPanel.add(Box.createVerticalStrut(25));

        
        JPanel detailsGrid = new JPanel(new GridLayout(1, 2, 25, 0));
        detailsGrid.setBackground(DashboardTheme.BG_MAIN);
        detailsGrid.setMaximumSize(new Dimension(2000, 300));

        detailsGrid.add(createDetailCard("Personal Information", new String[][]{
            {"Full Name", "Aman Gupta"},
            {"Email", "admin.sys@iiitd.ac.in"},
            {"Phone", "+91 98765 43210"},
            {"Employee ID", "EMP-9092"}
        }));

        detailsGrid.add(createDetailCard("Account Overview", new String[][]{
            {"Last Login", "26 Nov 2025, 10:30 AM"},
            {"Role Level", "Super Admin"},
            {"Permissions", "Full Access"},
            {"Office", "Academic Block, Room 204"}
        }));

        viewPanel.add(detailsGrid);
        
        return viewPanel;
    }

    private DashboardComponents.CardPanel createDetailCard(String titleStr, String[][] data) {
        DashboardComponents.CardPanel card = new DashboardComponents.CardPanel();
        card.setLayout(new BorderLayout());

        JLabel title = new JLabel(titleStr);
        title.setFont(DashboardTheme.FONT_SUBTITLE);
        title.setForeground(DashboardTheme.TEXT_PRIMARY); 
        title.setBorder(new EmptyBorder(15, 25, 15, 25));
        card.add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(4, 2, 10, 15));
        form.setOpaque(false);
        form.setBorder(new EmptyBorder(0, 25, 25, 25));

        for (String[] row : data) {
            JLabel lbl = new JLabel(row[0]);
            lbl.setFont(DashboardTheme.FONT_BOLD);
            lbl.setForeground(DashboardTheme.TEXT_SECONDARY);
            
            JLabel val = new JLabel(row[1]);
            val.setFont(DashboardTheme.FONT_REGULAR);
            val.setForeground(DashboardTheme.TEXT_PRIMARY); 
            
            form.add(lbl);
            form.add(val);
        }
        card.add(form, BorderLayout.CENTER);
        return card;
    }
}