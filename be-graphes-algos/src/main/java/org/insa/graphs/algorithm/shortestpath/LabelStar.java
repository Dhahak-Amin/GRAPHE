package org.insa.graphs.algorithm.shortestpath;
import org.insa.graphs.model.Node ;

public class LabelStar extends Label  {
    
    protected double cout_vol_oiseau ; 
    
    // Constructeur
    public LabelStar(Node sommet_courant) {
        super(sommet_courant) ; 
        //le coût du noeud vers la destination à vol d'oiseau on  initialisé à l'infini
        this.cout_vol_oiseau = Double.POSITIVE_INFINITY;
    }
    
    //fonction renvoyant le coût total du label: son coût + le trajet à vol d'oiseau vers la destination
    @Override
    public double getTotalCost( ) {
        return this.getCost()+this.cout_vol_oiseau;
    }
        
    //fonction renvoyant seulement le trajet à vol d'oiseau
  public double get_vol_oiseau() {
      return this.cout_vol_oiseau;
    }
    
    //fonction permettant de changer le cout du trajet à vol d'oiseau initialisé à l'infini (setter)
    public void set_vol_oiseau(double coût_vol_oiseau) {
        this.cout_vol_oiseau = coût_vol_oiseau;
    }

}