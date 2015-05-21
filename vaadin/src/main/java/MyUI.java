import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;

import javax.servlet.annotation.WebServlet;

@Theme("valo")
public class MyUI extends UI {
	@Override
	protected void init(VaadinRequest vaadinRequest) {
		final VerticalLayout mainLayout = new VerticalLayout();
		setContent(mainLayout);

		final BeanContainer<String, User> userBeanContainer = new BeanContainer<String, User>(User.class);

		userBeanContainer.setBeanIdProperty("login");
		userBeanContainer.addBean(new User(User.USER_FILE_1));
		userBeanContainer.addBean(new User(User.USER_FILE_2));
		userBeanContainer.addBean(new User(User.USER_FILE_3));
		userBeanContainer.addBean(new User(User.USER_FILE_4));

		final Table table = new Table();
		table.setSizeFull();
		table.setContainerDataSource(userBeanContainer);
		table.setEditable(false);
		mainLayout.addComponent(table);

		final Button buttonCommit = new Button("Edit");
		mainLayout.addComponent(buttonCommit);

		buttonCommit.addClickListener(new Button.ClickListener() {
			public void buttonClick(Button.ClickEvent clickEvent) {
				if (table.isEditable()) {
					table.commit();
					buttonCommit.setCaption("Edit");
					table.setEditable(false);
				} else {
					table.setEditable(true);
					buttonCommit.setCaption("Save");
				}
			}
		});
	}

	@WebServlet(urlPatterns = "/*")
	@VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
	public static class MyUIServlet extends VaadinServlet {
	}
}
