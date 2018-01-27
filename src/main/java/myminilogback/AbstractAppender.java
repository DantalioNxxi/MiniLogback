
package myminilogback;

/**
 * Abstract class of any Appender.
 * That object can to record logs(LogEvents).
 * @author DantalioNxxi
 * @see Appendable
 * @see ConsoleAppender
 * @see FileAppender
 * @see RollingFileAppender
 */
public abstract class AbstractAppender implements Appendable{
    protected String name;
    protected String classname;
    protected Layout layout;
    
    /**
     * Still so
     */
    protected AbstractAppender(){}

    @Override
    public Layout getLayout() {
        return layout;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getClassName() {
        return classname;
    }
    
    public void setLayout(Layout layout) {
        if (layout==null) this.layout = new SimpleLayout();
        else this.layout = layout;
    }

    @Override
    public String toString() {
        return "AbstractAppender{" + "name=" + name + ", classname=" + classname + ", layout=" + layout + '}';
    }
    
    
}
