
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

//    @Override
//    public Layout getLayout() {
//        return layout;
//    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getClassName() {
        return classname;
    }
    
    public void setLayout(Layout layout) {
        this.layout = layout;
    }

    @Override
    public String toString() {
        return "AbstractAppender{" + "name=" + name + ", classname=" + classname + ", layout=" + layout + '}';
    }
    
    
}
