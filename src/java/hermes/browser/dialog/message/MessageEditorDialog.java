package hermes.browser.dialog.message;

import hermes.Domain;
import hermes.browser.HermesBrowser;
import hermes.browser.components.EditedMessageHandler;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.TextMessage;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import org.apache.log4j.Logger;

import com.jidesoft.swing.JideTabbedPane;

public class MessageEditorDialog extends JDialog {
	private static final long serialVersionUID = 8270616550261160182L;
	private static final Logger log = Logger.getLogger(MessageEditorDialog.class);

	private final JPanel topPanel = new JPanel();
	private final String destinationName;
	private final Domain domain;
	private final EditedMessageHandler onOK;
	private final JideTabbedPane tabbedPane = new JideTabbedPane(JTabbedPane.TOP);

	private JMSHeaderPropertyPanel headerPropertyPanel;
	private UserHeaderPropertyPanel userHeaderPropertyPanel;
	private MessageWriter messageWriter;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			MessageEditorDialog dialog = new MessageEditorDialog(null, null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean canEdit(Message message) {
		return message == null || message instanceof Message || message instanceof TextMessage || message instanceof MapMessage || message instanceof BytesMessage;
	}

	/**
	 * Create the dialog.
	 */

	public MessageEditorDialog(final Message message, String destinationName, Domain domain, final EditedMessageHandler onOK) throws JMSException {
		this("Send message to " + destinationName, message, destinationName, domain, onOK, "Send", "Done");

		setModal(false);
	}

	public MessageEditorDialog(final Message message, final EditedMessageHandler onOK) throws JMSException {
		this("Edit Message", message, null, null, onOK, "OK", "Cancel");
		setModal(true);
	}

	public MessageEditorDialog(final String title, final Message message, String destinationName, Domain domain, final EditedMessageHandler onOK, String okText, String cancelText) throws JMSException {
		super(HermesBrowser.getBrowser());
		if (!canEdit(message)) {
			throw new JMSException("Unsupported message type");
		}
		this.onOK = onOK;
		this.destinationName = destinationName;
		this.domain = domain;

		setTitle(title);
		setBounds(100, 100, 700, 400);

		topPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		topPanel.setLayout(new BorderLayout(0, 0));

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(topPanel, BorderLayout.CENTER);

		{
			topPanel.add(tabbedPane);
			{
				headerPropertyPanel = new JMSHeaderPropertyPanel();
				JPanel panel = new JPanel(new GridBagLayout());
				panel.add(headerPropertyPanel);
				tabbedPane.addTab("JMS Header", null, panel, null);
			}
			{
				userHeaderPropertyPanel = new UserHeaderPropertyPanel(message);
				tabbedPane.addTab("User Header", null, userHeaderPropertyPanel, null);
			}
		}

		headerPropertyPanel.setMessage(message, destinationName, domain);

		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton(okText);
				okButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						if (isModal()) {
							MessageEditorDialog.this.dispose();
						}
						if (onOK != null) {
							onOK();
						}
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton(cancelText);
				cancelButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						MessageEditorDialog.this.dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}

		if (message == null) {
			messageWriter = new TextMessagePayloadPanel();
		} else if (message instanceof TextMessage) {
			messageWriter = new TextMessagePayloadPanel((TextMessage) message);
		} else if (message instanceof MapMessage) {
			messageWriter = new MapMessagePayloadPanel((MapMessage) message, true);
		} else if (message instanceof BytesMessage) {
			messageWriter = new BytesMessagePayloadPanel((BytesMessage) message);
		} else {
			messageWriter = new CannotEditMessageWriter(message);
		}

		tabbedPane.addTab("Payload", messageWriter);

		headerPropertyPanel.getMessageTypeComboBox().getModel().addListDataListener(new ListDataListener() {
			@Override
			public void intervalRemoved(ListDataEvent e) {
			}

			@Override
			public void intervalAdded(ListDataEvent e) {
			}

			@Override
			public void contentsChanged(ListDataEvent e) {
				MessageType type = (MessageType) headerPropertyPanel.getMessageTypeComboBox().getModel().getSelectedItem();

				if (messageWriter != null) {
					tabbedPane.remove(messageWriter);
				}
				try {
					messageWriter = createWriterForType(type);
					if (messageWriter != null) {
						tabbedPane.addTab("Payload", messageWriter);
					}
				} catch (JMSException ex) {
					HermesBrowser.getBrowser().showErrorDialog(ex);
				}
			}
		});
	}

	private MessageWriter createWriterForType(MessageType type) throws JMSException {
		switch (type) {
		case TextMessage:
			return new TextMessagePayloadPanel();
		case MapMessage:
			return new MapMessagePayloadPanel();
		case BytesMessage:
			return new BytesMessagePayloadPanel();
		}
		return new CannotEditMessageWriter();
	}

	protected void onOK() {
		try {
			Message newMessage = headerPropertyPanel.createMessage(onOK);
			userHeaderPropertyPanel.setProperties(newMessage);
			if (messageWriter != null) {
				messageWriter.onMessage(newMessage);
			}
			onOK.onMessage(newMessage);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			HermesBrowser.getBrowser().showErrorDialog(ex.getMessage(), ex);
		}
	}

}
