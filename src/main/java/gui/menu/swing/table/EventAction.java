package gui.menu.swing.table;

import gui.menu.model.ModelStudent;

public interface EventAction {

    public void delete(ModelStudent student);

    public void update(ModelStudent student);
}
