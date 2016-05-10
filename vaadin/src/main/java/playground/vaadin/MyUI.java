package playground.vaadin;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;

import javax.servlet.annotation.WebServlet;

@Theme("valo")
public class MyUI extends UI {
	private static final UserDao USER_DAO = new UserDao();
	private final User user1 = USER_DAO.read(User.USER_FILE_1);
	private final User user2 = USER_DAO.read(User.USER_FILE_2);
	private final User user3 = USER_DAO.read(User.USER_FILE_3);
	private final User user4 = USER_DAO.read(User.USER_FILE_4);

	@Override
	protected void init(VaadinRequest vaadinRequest) {
		final VerticalLayout mainLayout = new VerticalLayout();
		setContent(mainLayout);

		final BeanContainer<String, User> userBeanContainer = new BeanContainer<String, User>(User.class);

		userBeanContainer.setBeanIdProperty("login");
		userBeanContainer.addBean(user1);
		userBeanContainer.addBean(user2);
		userBeanContainer.addBean(user3);
		userBeanContainer.addBean(user4);

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

					USER_DAO.write(user1, User.USER_FILE_1);
					USER_DAO.write(user2, User.USER_FILE_2);
					USER_DAO.write(user3, User.USER_FILE_3);
					USER_DAO.write(user4, User.USER_FILE_4);
				} else {
					table.setEditable(true);
					buttonCommit.setCaption("Save");
				}
			}
		});

//		Chart chart = new Chart(ChartType.COLUMN);
//		chart.setWidth("400px");  // 100% by default
//		chart.setHeight("300px"); // 400px by default
//		mainLayout.addComponent(chart);

		mainLayout.addComponent(buildTree());
	}


	private Tree buildTree()
	{
		final Tree tree = new Tree("Navigation");
		tree.setContainerDataSource(buildTreeContainerDataSource());
		tree.setImmediate(true);
		tree.setItemCaptionPropertyId("1234");
		tree.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
		tree.expandItem(0);

		return tree;
	}

	private Container buildTreeContainerDataSource()
	{
		final String property = "1234";
		final HierarchicalContainer container = new HierarchicalContainer();
		container.addContainerProperty(property, String.class, "");

		Item item = container.addItem(0);
		container.setChildrenAllowed(0, true);
		item.getItemProperty(property).setValue("google.com");

		item = container.addItem(1);
		item.getItemProperty(property).setValue("doc1");
		container.setParent(1, 0);
		container.setChildrenAllowed(1, false);

		return container;
	}

	@WebServlet(urlPatterns = "/*")
	@VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
	public static class MyUIServlet extends VaadinServlet {
	}
}
