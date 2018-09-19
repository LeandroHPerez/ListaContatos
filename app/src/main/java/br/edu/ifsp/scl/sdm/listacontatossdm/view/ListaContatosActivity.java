package br.edu.ifsp.scl.sdm.listacontatossdm.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifsp.scl.sdm.listacontatossdm.R;
import br.edu.ifsp.scl.sdm.listacontatossdm.adapter.ListaContatosAdapter;
import br.edu.ifsp.scl.sdm.listacontatossdm.model.Contato;

public class ListaContatosActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    //REQUEST_CODE para a abertura da tela ContatoActivity - MODO NOVO CONTATO
    private final int NOVO_CONTATO_REQUEST_CODE = 0;

    //constante para passar parâmetros para a tela ContatoActivity - MODO DETALHES
    public static final String CONTATO_EXTRA = "CONTATO_EXTRA";


    public static final String CONTATO_EDITAR_EXTRA = "CONTATO_EDITAR_EXTRA";

    private ListView listaContatosListView;

    // Lista de contatos usada para preencher ListView
    private List<Contato> listaContatos;

    //Adapter
    private ListaContatosAdapter listaContatosAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_contatos);

        //Ref para a ListView
        listaContatosListView = findViewById(R.id.listaContatosListView);

        listaContatos = new ArrayList<>();
//        preencheListaContatos();


        /*
        List<String> listaNome = new ArrayList<>();
        for(Contato contato : listaContatos) {
            listaNome.add(contato.getNome());
        }

        //Criação de um adaptador
        //ArrayAdapter<Contato> listaContatosAdapter =  new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaContatos);

        ArrayAdapter<String> listaContatosAdapter =  new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaNome);
        //informando o adaptador para o listView
        listaContatosListView.setAdapter(listaContatosAdapter);
*/


        listaContatosAdapter = new ListaContatosAdapter(this, listaContatos);
        listaContatosListView.setAdapter(listaContatosAdapter);

        //Faz o menu de contexto aparecer na listview de contatos
        registerForContextMenu(listaContatosListView);

        listaContatosListView.setOnItemClickListener(this);


    }




    private void preencheListaContatos(){
        for (int i = 0; i < 10; i++){
            listaContatos.add(new Contato("C" + i, "ifsp", "1234", "i@ifsp"));
        }
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.configuracaoMenuItem:
                return true;
            case R.id.novoContatoMenuItem:
                //Abrindo tela de novo contato
                Intent novoContatoIntent = new Intent(this, ContatoActivity.class);
                //Intent novoContatoIntent = new Intent("NOVO_CONTATO_ACTION"); //chamada via action que fica no mafifest com filter
                startActivityForResult(novoContatoIntent, NOVO_CONTATO_REQUEST_CODE);
                return true;
            case R.id.sairMenuItem:
                finish();
                return true;
        }
        return false;
    }




    //trata os retorno de activity que são chamadas via intent
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case NOVO_CONTATO_REQUEST_CODE:
                if(resultCode == RESULT_OK){
                    // recupera o contato da Intent data

                    Contato novoContato = (Contato) data.getSerializableExtra(ListaContatosActivity.CONTATO_EXTRA);


                    //Atualizo lista
                    if (novoContato != null){
                        listaContatos.add(novoContato);
                        listaContatosAdapter.notifyDataSetChanged(); //avisa o adapter dealteração na lista de dados

                        Toast.makeText(this, "Novo contato adicionado!", Toast.LENGTH_SHORT).show();
                    }


                    //notifico o adapter
                } else {
                    if(resultCode == RESULT_CANCELED){
                        //não faria nada
                        Toast.makeText(this, "Cadastro cancelado!", Toast.LENGTH_SHORT).show();

                    }
                }
        }
    }





    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.menu_contexto, menu);
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo infoMenu = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        //como o menu é de contexto
        Contato contato = listaContatos.get(infoMenu.position);

        switch (item.getItemId()){
            case R.id.editarContatoMenuItem:


                Intent detalhesContatoIntent = new Intent(this, ContatoActivity.class);
                detalhesContatoIntent.putExtra(CONTATO_EDITAR_EXTRA, contato);
                startActivity(detalhesContatoIntent);

                return true;
            case R.id.ligarParaContatoMenuItem:
                return true;
            case R.id.verEnderecoMenuItem:
                return true;
            case R.id.enviarEmailMenuItem:
                return true;
            case R.id.removerContatolMenuItem:
                return true;
        }

        return false;
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int posicao, long l){
        Contato contato = listaContatos.get(posicao);

        Intent detalhesContatoIntent = new Intent(this, ContatoActivity.class);
        detalhesContatoIntent.putExtra(CONTATO_EXTRA, contato);
        startActivity(detalhesContatoIntent);
    }
}
