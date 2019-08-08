package edu.cmu.cs.cs214.hw5.framework.plugin.control;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import java.awt.Dimension;
import java.awt.Toolkit;

/**
 * The {@code ApplyButton} class is a useful GUI element for plugin writers.
 * The apply button should be used when a time-consuming task will be performed
 * by pressing the apply button. The apply button will perform the task off
 * the Event Dispatch Thread(EDT), to prevent freezing the GUI. It will also generate
 * a waiting message telling the user to wait until the task is done.
 * <p>
 * The apply SHOULD be used by the data plugin to perform the {@code loadAccount} task.
 * Because the {@code loadAccount} task usually takes from several seconds up to several
 * minutes depend on the length of the input, and the GUI should not freeze during
 * the waiting period.
 */
public class ApplyButton extends JButton {
    private Runnable target;

    private final JOptionPane optionPane;

    private final JDialog dialog;

    /**
     * Initialize the {@code ApplyButton} object from a runnable target, and a message.
     *
     * @param target        The target task to perform when the button is pressed
     * @param message       The message to show when the user is waiting for the task to complete
     */
    public ApplyButton(Runnable target, String message) {
        this.target = target;

        optionPane = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE,
                JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);

        dialog = new JDialog();

        dialog.setTitle("Message");
        dialog.setModal(true);

        dialog.setContentPane(optionPane);

        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialog.pack();


        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - dialog.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - dialog.getHeight()) / 2);
        dialog.setLocation(x, y);
        dialog.setResizable(false);

        setText("Apply");

        addActionListener(e -> {
            getNewWorker().execute();
            dialog.setVisible(true);
        });
    }

    private SwingWorker getNewWorker() {
        return new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                target.run();
                return null;
            }

            @Override
            protected void done() {
                super.done();
                dialog.setVisible(false);
                dialog.dispose();
            }
        };
    }
}
