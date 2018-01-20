
package myminilogback;

/**
 *
 * @author DantalioNxxi
 */
public abstract class AbstractAppender implements Appendable{
    protected String name;
    protected String classname;
    protected Layout layout;
    
    /**
     * Still so
     */
    protected AbstractAppender(){}

    public Layout getLayout() {
        return layout;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
    }
}
