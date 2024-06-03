package rea.components;

public interface Visitor {
    void visit(Character character);
    void visit(Item item);
    void visit(Passage passage);
    void visit(Place place);
}
