package br.edu.ifsp.scl.sdm.listacontatossdm.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifsp.scl.sdm.listacontatossdm.R;
import br.edu.ifsp.scl.sdm.listacontatossdm.adapter.ListaContatosAdapter;
import br.edu.ifsp.scl.sdm.listacontatossdm.model.Contato;
import br.edu.ifsp.scl.sdm.listacontatossdm.util.ArmazenamentoHelper;
import br.edu.ifsp.scl.sdm.listacontatossdm.util.Configuracoes;
import br.edu.ifsp.scl.sdm.listacontatossdm.util.JsonHelper;

public class ListaContatosActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    //REQUEST_CODE para a abertura da tela ContatoActivity - MODO NOVO CONTATO
    private final int NOVO_CONTATO_REQUEST_CODE = 0;

    private final int CONFIGURACAO_REQUEST_CODE = 1;

    //REQUEST_CODE para a abertura da tela ContatoActivity - MODO EDIÇÃO DE CONTATO
    private final int EDICAO_CONTATO_REQUEST_CODE = 2;

    //constante para passar parâmetros para a tela ContatoActivity - MODO DETALHES ou SALVAR
    public static final String CONTATO_EXTRA = "CONTATO_EXTRA";

    //constante para passar parâmetros para a tela ContatoActivity - MODO EDITAR
    public static final String CONTATO_EDITAR_EXTRA = "CONTATO_EDITAR_EXTRA";
    public static final String LIST_VIEW_INDICE = "LIST_VIEW_INDICE";

    private ListView listaContatosListView;

    // Lista de contatos usada para preencher ListView
    private List<Contato> listaContatos;

    //Adapter que preenche ListView
    private ListaContatosAdapter listaContatosAdapter;

    //Shared preferences
    private SharedPreferences sharedPreferences;
    private final String CONFIGURACOES_SHARED_PREFERENCES = "CONFIGURACOES";
    private final String TIPO_ARMAZENAMENTO_SHARED_PREFERENCES = "TIPO_ARMAZENAMENTO";


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

        //Registra o listener para clique em itens da lista
        listaContatosListView.setOnItemClickListener(this);



        sharedPreferences = getSharedPreferences(CONFIGURACOES_SHARED_PREFERENCES, MODE_PRIVATE);
        restauraConfiguracoes();


        restauraContatos();


    }

    private void restauraContatos() {

        try {
            JSONArray jsonArray = ArmazenamentoHelper.buscarContatos(this, Configuracoes.getInstance().getTipoArmazenamento());

            if (jsonArray != null) {
                List<Contato> contatosSalvosList = JsonHelper.jsonArrayParaListaContatos(jsonArray);
                listaContatos.addAll(contatosSalvosList);
                listaContatosAdapter.notifyDataSetChanged();
                ;
            }

        }catch(Exception e){
            e.printStackTrace();
        }

    }


    private void restauraConfiguracoes() {
        int tipoArmazenamento = sharedPreferences.getInt(TIPO_ARMAZENAMENTO_SHARED_PREFERENCES, Configuracoes.ARMAZENAMENTO_INTERNO);
        Configuracoes.getInstance().setTipoArmazenamento(tipoArmazenamento);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        salvaConfiguracoes();

        try{
            JSONArray jsonArray = JsonHelper.listaContatosParaJsonArray(listaContatos);
            if(jsonArray != null){
                ArmazenamentoHelper.salvarContatos(this, Configuracoes.getInstance().getTipoArmazenamento(), jsonArray);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    //salva as info no shared preferences
    private void salvaConfiguracoes() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(TIPO_ARMAZENAMENTO_SHARED_PREFERENCES, Configuracoes.getInstance().getTipoArmazenamento());
        editor.commit();
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
                Intent configuracoesIntent = new Intent(this, ConfiguracoesActivity.class);
                startActivityForResult(configuracoesIntent, CONFIGURACAO_REQUEST_CODE);
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
            //Trata Resultado da inclusão do contato
            case NOVO_CONTATO_REQUEST_CODE: //Novo Contato
                if(resultCode == RESULT_OK){

                    // recupera o contato da Intent data
                    Contato novoContato = (Contato) data.getSerializableExtra(ListaContatosActivity.CONTATO_EXTRA);


                    //Atualizo lista com o novo contato
                    if (novoContato != null){
                        listaContatos.add(novoContato);
                        listaContatosAdapter.notifyDataSetChanged(); //avisa o adapter sobre a alteração na lista de dados

                        Toast.makeText(this, "Novo contato adicionado!", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    if(resultCode == RESULT_CANCELED){
                        //não faria nada
                        Toast.makeText(this, "Cadastro cancelado!", Toast.LENGTH_SHORT).show();

                    }
                }
                break;

            //Trata Resultado da edição do contato
            case EDICAO_CONTATO_REQUEST_CODE: //Editar Contato
                if(resultCode == RESULT_OK){

                    //Toast.makeText(this, "Modo edição", Toast.LENGTH_SHORT).show();

                    // recupera o contato da Intent data
                    Contato editarContato = (Contato) data.getSerializableExtra(ListaContatosActivity.CONTATO_EDITAR_EXTRA);
                    int indiceListViewEdicao = data.getIntExtra(LIST_VIEW_INDICE, -1);


                    //Atualizo lista com o contato editado
                    if (editarContato != null){
                        if (indiceListViewEdicao != -1) {
                            listaContatos.set(indiceListViewEdicao, editarContato); //Atualizsa a lista com o contato editado
                            Toast.makeText(this, "Contato Atualizado!", Toast.LENGTH_SHORT).show();
                        }
                        listaContatosAdapter.notifyDataSetChanged(); //avisa o adapter sobre a alteração na lista de dados


                    }

                } else {
                    if(resultCode == RESULT_CANCELED){
                        //não faria nada
                        Toast.makeText(this, "Edição cancelada!", Toast.LENGTH_SHORT).show();

                    }
                }
                break;


        }
    }





    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.menu_contexto, menu);
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        //Ontém objeto de informações do menu de contexto
        AdapterView.AdapterContextMenuInfo infoMenu = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        //como o menu é de contexto
        Contato contato = listaContatos.get(infoMenu.position);

        switch (item.getItemId()){
            case R.id.editarContatoMenuItem:


                Intent detalhesContatoIntent = new Intent(this, ContatoActivity.class);
                detalhesContatoIntent.putExtra(CONTATO_EDITAR_EXTRA, contato);
                detalhesContatoIntent.putExtra(LIST_VIEW_INDICE, infoMenu.position); //passa a posição do contato editado


                startActivityForResult(detalhesContatoIntent, EDICAO_CONTATO_REQUEST_CODE);

                return true;
            case R.id.ligarParaContatoMenuItem:
                Uri telefoneUri = Uri.parse("tel:" + contato.getTelefone());
                Intent ligarContato = new Intent(Intent.ACTION_DIAL, telefoneUri);
                startActivity(ligarContato);

                return true;
            case R.id.verEnderecoMenuItem:
                Uri enderecoUri = Uri.parse("geo:0,0?q=" + contato.getEndereco());
                Intent verEnderecoView = new Intent(Intent.ACTION_VIEW, enderecoUri);
                startActivity(verEnderecoView);
                return true;
            case R.id.enviarEmailMenuItem:
                Uri emailUri = Uri.parse("mailto:" + contato.getEmail());
                Intent enviarEmailIntent = new Intent(Intent.ACTION_SENDTO, emailUri);
                startActivity(enviarEmailIntent);
                return true;
            case R.id.removerContatolMenuItem:
                removerContato(infoMenu.position); //posição do contato para ser removido
                return true;
        }

        return false;
    }

    private void removerContato(final int posicao) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setMessage("Confirmar remoção?");

        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listaContatos.remove(posicao);
                listaContatosAdapter.notifyDataSetChanged();
            }
        });


        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //faz nada
            }
        });


        AlertDialog removeAlertDialog = builder.create();
        removeAlertDialog.show();

    }

    //Trata clique em itens da lista
    public void onItemClick(AdapterView<?> adapterView, View view, int posicao, long l){
        Contato contato = listaContatos.get(posicao);

        //Abre a tela de detalhes do item clicado
        Intent detalhesContatoIntent = new Intent(this, ContatoActivity.class);
        detalhesContatoIntent.putExtra(CONTATO_EXTRA, contato);
        startActivity(detalhesContatoIntent);
    }
}
