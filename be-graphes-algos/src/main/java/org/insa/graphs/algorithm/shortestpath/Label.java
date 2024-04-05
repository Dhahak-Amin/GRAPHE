package org.insa.graphs.algorithm.shortestpath;
import org.insa.graphs.model.*;

public class Label {
    
    private Node sommet_courant;
    private double cout_realise;
    private boolean marque;
    private Arc pere;

    // le constructeur :
    public Label(Node sommet_courant, double cout_realise, boolean marque, Arc pere){
        this.sommet_courant = sommet_courant;
        this.marque = false;
        this.cout_realise = Double.POSITIVE_INFINITY;
        this.pere = null;
    }

    // les setters : 
    public void setSommetCourant(Node sommet_courant) {
        this.sommet_courant = sommet_courant;
    }

    public void setCoutRealise(double cout_realise) {
        this.cout_realise = cout_realise;
    }

    public void setMarque(boolean marque) {
        this.marque = marque;
    }

    public void setPere(Arc pere) {
        this.pere = pere;
    }


    // les getters :
    public Node getSommetCourant() {
        return this.sommet_courant;
    }

    public Arc getPere() {
        return this.pere;
    }

    
    public boolean isMarque() {
        return this.marque;
    }

    public double getCost() {
        return this.cout_realise;
    }

}
