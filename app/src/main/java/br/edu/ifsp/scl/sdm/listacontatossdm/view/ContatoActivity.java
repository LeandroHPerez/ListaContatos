package br.edu.ifsp.scl.sdm.listacontatossdm.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import br.edu.ifsp.scl.sdm.listacontatossdm.R;
import br.edu.ifsp.scl.sdm.listacontatossdm.model.Contato;

public class ContatoActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText nomeEditText;
    private EditText enderecoEditText;
    private EditText telefoneEditText;
    private EditText emailEditText;
    private Button cancelarButton;
    private Button salvarButton;

    Contato contatoParaEdicao;

    //Usado para manter o índice da lista de contatos que deverá ser atualizado - MODO EDIÇÃO
    private int indiceListViewEdicao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contato);


        contatoParaEdicao = null;


        //Buscando refrências de layout
        nomeEditText = findViewById(R.id.nomeEditText);
        enderecoEditText = findViewById(R.id.enderecoEditText);
        telefoneEditText = findViewById(R.id.telefoneEditText);
        emailEditText = findViewById(R.id.emailEditText);
        cancelarButton = findViewById(R.id.cancelarButton);
        salvarButton = findViewById(R.id.salvarButton);

        //Setando Listeners dos botões
        cancelarButton.setOnClickListener(this);
        salvarButton.setOnClickListener(this);

        String subtitulo;
        //Obtém da Intent o objeto Contato passado no clique de detalhamento
        Contato contato = (Contato) getIntent().getSerializableExtra(ListaContatosActivity.CONTATO_EXTRA);

        //Obtém da Intent o objeto Contato passado no clique de menu de contexto para a Edição
        contatoParaEdicao = (Contato) getIntent().getSerializableExtra(ListaContatosActivity.CONTATO_EDITAR_EXTRA);

        if (contatoParaEdicao != null) {
            //Modo edição

            subtitulo = "Edição do contato";
            modoEdicao(contatoParaEdicao);


            indiceListViewEdicao = getIntent().getIntExtra(ListaContatosActivity.LIST_VIEW_INDICE, -1);
        }

        if (contato != null) {
            //Modo detalhes

            subtitulo = "Detalhes do contato";
            modoDetalhes(contato);

        } else {
            //modo cadastro

            subtitulo = "Novo contato";
        }

        //setando subtítulo
        getSupportActionBar().setSubtitle(subtitulo);
    }

    private void modoDetalhes(Contato contato) {
        nomeEditText.setText(contato.getNome());
        nomeEditText.setEnabled(false);

        enderecoEditText.setText(contato.getEndereco());
        enderecoEditText.setEnabled(false);

        telefoneEditText.setText(contato.getTelefone());
        telefoneEditText.setEnabled(false);

        emailEditText.setText(contato.getEmail());
        emailEditText.setEnabled(false);

        salvarButton.setVisibility(View.GONE);
    }


    private void modoEdicao(Contato contato) {
        nomeEditText.setText(contato.getNome());
        nomeEditText.setEnabled(true);

        enderecoEditText.setText(contato.getEndereco());
        enderecoEditText.setEnabled(true);

        telefoneEditText.setText(contato.getTelefone());
        telefoneEditText.setEnabled(true);

        emailEditText.setText(contato.getEmail());
        emailEditText.setEnabled(true);

        salvarButton.setVisibility(View.VISIBLE);
    }

    //Trata os cliques do botão Cancelar e Salvar
    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.cancelarButton:
                setResult(RESULT_CANCELED);
                finish(); //fecha a tela atual
                break;

            case R.id.salvarButton:
                Contato novoContato = new Contato(); //novoContato serve para a edição também, não quiz mudar o nome para não "confundir" o código base ensinado em aula pelo professor
                novoContato.setNome((nomeEditText.getText().toString()));
                novoContato.setEndereco(enderecoEditText.getText().toString());
                novoContato.setTelefone(telefoneEditText.getText().toString());
                novoContato.setEmail(emailEditText.getText().toString());

                Intent resultadoIntent = new Intent();
                if (contatoParaEdicao != null) {    //modo edição

                    resultadoIntent.putExtra(ListaContatosActivity.CONTATO_EDITAR_EXTRA, novoContato); //modo edição
                    resultadoIntent.putExtra(ListaContatosActivity.LIST_VIEW_INDICE, indiceListViewEdicao);
                }else {                             //modo cadastro
                    resultadoIntent.putExtra(ListaContatosActivity.CONTATO_EXTRA, novoContato); //modo cadastro
                }

                setResult(RESULT_OK, resultadoIntent);
                finish(); //fecha a tela
                break;
        }


    }
}
