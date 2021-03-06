package br.edu.ifsp.scl.sdm.listacontatossdm.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import br.edu.ifsp.scl.sdm.listacontatossdm.R;
import br.edu.ifsp.scl.sdm.listacontatossdm.model.Contato;

public class ListaContatosAdapter extends ArrayAdapter<Contato> {

    private LayoutInflater layoutInflater;

    public ListaContatosAdapter(Context context, List<Contato> listaContatos){
        super(context, R.layout.layout_view_contato_adapter, listaContatos);
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Holder holder;


        //Se ainda não foi inflada
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.layout_view_contato_adapter, null);

            //Cria um holder para célula nova
            holder = new Holder();
            holder.nomeContatoTextView = convertView.findViewById(R.id.nomeContatoTextView);
            holder.emailContatoTextView = convertView.findViewById(R.id.emailContatoTextView);

            //guarda no "bolso" da view criada o holder
            convertView.setTag(holder);

        }
        else {
            //recupera o holder do bolso de uma view reaproveitada
            holder = (Holder) convertView.getTag();
        }



        //Métodos antigos de baixa performance
        //TextView nomeContatoTextView = convertView.findViewById(R.id.nomeContatoTextView);
        //TextView emailContatoTextView = convertView.findViewById(R.id.emailContatoTextView);

        //nomeContatoTextView.setText(contato.getNome());
        //emailContatoTextView.setText(contato.getEmail());



        //Vai na lista passada como parâmetro e retorna o elemento da posição
        Contato contato = getItem(position);
        holder.nomeContatoTextView.setText(contato.getNome());
        holder.emailContatoTextView.setText(contato.getEmail());

        return convertView;
    }



    private class Holder{
        public TextView nomeContatoTextView;
        public TextView emailContatoTextView;
    }
}
