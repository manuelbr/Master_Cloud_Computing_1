package com.example.manuel.prototipo_personaemocion.Actividades.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.manuel.prototipo_personaemocion.Model.Pelicula;
import com.example.manuel.prototipo_personaemocion.R;

import java.util.ArrayList;

/**
 * Created by Manuel on 03/12/2015.
 */
public class expandableList extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<String> padres;
    private ArrayList<ArrayList<Pelicula> > hijos;
    private ArrayList<Pelicula> hijos2;
    private boolean sugiriendo;

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    public  expandableList(Context contexto, ArrayList<Pelicula> hijos){
        context = contexto;
        hijos2 = hijos;
        sugiriendo = true;
        padres = new ArrayList<String>();
        padres.add("Películas Sugeridas (En Orden)");
    }

    public expandableList(Context contexto, ArrayList<ArrayList<Pelicula> > hijoss, ArrayList<String> padress) {
        context = contexto;
        hijos = hijoss;
        padres = padress;
        sugiriendo = false;
    }

    public void addItem(Pelicula item) {
        if(!sugiriendo) {
            if (!padres.contains(item.getGenero())) {
                padres.add(item.getGenero());
                ArrayList<Pelicula> p = new ArrayList<Pelicula>();
                p.add(item);
                hijos.add(p);
            } else {
                int index = padres.indexOf(item.getGenero());
                hijos.get(index).add(item);
            }
        }else{
            hijos2.add(item);
        }
    }

    @Override
    public Pelicula getChild(int posicionPadre, int posicionHijo) {
        if(!sugiriendo) {
            return hijos.get(posicionPadre).get(posicionHijo);
        }else{
            return hijos2.get(posicionHijo);
        }
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int posicionPadre, int posicionHijo, boolean esUltimoHijo, View convertView, ViewGroup padre) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.childlayout, null);
        }

        TextView textview = (TextView) convertView.findViewById(R.id.tvTitle);
        ImageView imageview = (ImageView) convertView.findViewById(R.id.ivItem);

        Pelicula p;

        if (!sugiriendo) {
            p = hijos.get(posicionPadre).get(posicionHijo);
        }else{
            p = hijos2.get(posicionHijo);
        }

        imageview.setImageBitmap(p.getCaratula());

        ImageView emocion = null;
        for(int i=0;i<3;i++){
            switch(i){
                case 0: emocion = (ImageView) convertView.findViewById(R.id.emocion1);
                    break;
                case 1: emocion = (ImageView) convertView.findViewById(R.id.emocion2);
                    break;
                case 2: emocion = (ImageView) convertView.findViewById(R.id.emocion3);
                    break;
            }


            switch(p.getEmocionesPredominantes().get(i+p.getEmocionesPredominantes().size()-3)){
                case "Ira":emocion.setImageResource(R.drawable.ira);
                    break;
                case "Tristeza": emocion.setImageResource(R.drawable.triste);
                    break;
                case "Temor": emocion.setImageResource(R.drawable.miedo);
                    break;
                case "Placer": emocion.setImageResource(R.drawable.placer);
                    break;
                case "Amor": emocion.setImageResource(R.drawable.amor);
                    break;
                case "Sorpresa": emocion.setImageResource(R.drawable.sorpresa);
                    break;
                case "Disgusto": emocion.setImageResource(R.drawable.disgusto);
                    break;
                case "Verguenza": emocion.setImageResource(R.drawable.verguenza);
                    break;
                case "Alegria": emocion.setImageResource(R.drawable.alegria);
                    break;
            }
        }

        TextView director = (TextView) convertView.findViewById(R.id.director);
        director.setText(p.getDirector());

        TextView año = (TextView) convertView.findViewById(R.id.año);
        año.setText(Integer.toString(p.getAño()));

        textview.setText(p.getTitulo());
        textview.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

        return convertView;
    }

    @Override
    public int getChildrenCount(int posicionPadre) {
        if(!sugiriendo){
            return hijos.get(posicionPadre).size();
        }else{
            return hijos2.size();
        }
    }

    @Override
    public Object getGroup(int posicionPadre) {
        return padres.get(posicionPadre);
    }

    @Override
    public int getGroupCount() {
        return padres.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
    @Override
    public View getGroupView(int posicionPadre, boolean isExpanded, View convertView, ViewGroup padre) {

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.grouplayout, null);
        }

        TextView textview = (TextView) convertView.findViewById(R.id.tvGroup);
        textview.setText(padres.get(posicionPadre));
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int arg0, int arg1) {
        return true;
    }


}