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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contato);


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
        Contato contato = (Contato) getIntent().getSerializableExtra(ListaContatosActivity.CONTATO_EXTRA);


        Contato contatoParaEdicao = (Contato) getIntent().getSerializableExtra(ListaContatosActivity.CONTATO_EDITAR_EXTRA);

        if (contatoParaEdicao != null) {
            //Modo edição

            subtitulo = "Edição do contato";
            modoEdicao(contatoParaEdicao);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.cancelarButton:
                setResult(RESULT_CANCELED);
                finish(); //fecha a tela atual
                break;

            case R.id.salvarButton:
                Contato novoContato = new Contato();
                novoContato.setNome((nomeEditText.getText().toString()));
                novoContato.setEndereco(enderecoEditText.getText().toString());
                novoContato.setTelefone(telefoneEditText.getText().toString());
                novoContato.setEmail(emailEditText.getText().toString());

                Intent resultadoIntent = new Intent();
                resultadoIntent.putExtra(ListaContatosActivity.CONTATO_EXTRA, novoContato);

                setResult(RESULT_OK, resultadoIntent);
                finish(); //fecha a tela
                break;
        }


    }
}
