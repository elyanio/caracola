package com.polymitasoft.caracola.view.service;

/**
 * @author yanio
 * @since 04/01/2017
 */
public class DialogoListaSI {

//    private final String MARCA_LETRA = "#";
//    ArrayList<InternalService> datos;
//    ArrayList<String> buscar;
//    private Context context;
//    private ArrayList<String> iniciales;
//    // controles
//    private ListView servicesList;
//    private AutoCompleteTextView text_buscar;
//    private Button bt_buscar;
//
//    private EntityDataStore<Persistable> dataStore;
//
//    public DialogoListaSI(Context context) {
//        super(context);
//        this.context = context;
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.cliente_dialogo_pais);
//        iniciales = new ArrayList<Inicial>();
//        obtener_controles();
//        crearDao();
//        obtenerDatosyBuscar();
//        configurar_controles();
//        //eventos
//        evento();
//    }
//
//    private void obtener_controles() {
//        servicesList = (ListView) findViewById(R.id.cliente_lista_pais);
//        text_buscar = (AutoCompleteTextView) findViewById(R.id.cliente_text_buscar);
//        bt_buscar = (Button) findViewById(R.id.cliente_bt_buscar_pais);
//    }
//
//    private void crearDao() {
//        try {
//            dataStore = new Servicio_Interno_Dao(context);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void obtenerDatosyBuscar() {
//        buscar = new ArrayList<String>();
//        datos = new ArrayList<Servicio_Interno>();
//        datos = dataStore.obtener_todos_servicios_internos();
//        for (Servicio_Interno servicio_interno : datos) {
//            buscar.add(servicio_interno.getNombre());
//        }
//    }
//
//    private void configurar_controles() {
//
//        configurar_iniciales_y_datos();
//        configurar_lista_SI();
//        configurar_lista_letras(); // toma los datos de lista_secciones
//        //configurar el buscar
//        ArrayAdapter<String> aa = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, buscar);
//        text_buscar.setAdapter(aa);
//        // el boton comienza desabilitado
//        bt_buscar.setEnabled(false);
//
//    }
//
//    private void configurar_iniciales_y_datos() {
//        int cant = 0;
//        Inicial temp;
//        for (int i = 0; i < datos.size(); i++) {
//            String s = datos.get(i).getNombre().substring(0, 1).toUpperCase();
//            temp = new Inicial(s);
//            if (!iniciales.contains(temp)) {
//                cant = 1;
//                temp.setCantidad(cant);
//                iniciales.add(temp);
//                Servicio_Interno servicio_interno = new Servicio_Interno(s + "#", 0.0);
//                datos.add(i, servicio_interno);
//                i++;
//            } else {
//                cant++;
//                iniciales.get(iniciales.size() - 1).setCantidad(cant);
//            }
//        }
//    }
//
//    private void configurar_lista_SI() {
//        AdaptadorSI adaptador = new AdaptadorSI((Activity) context, android.R.layout.simple_list_item_1, datos);
//        servicesList.setAdapter(adaptador);
//    }
//
//    private void configurar_lista_letras() {
//        Adaptador_Lista_Letras adaptador = new Adaptador_Lista_Letras((Activity) context, iniciales);
//        lista_letras.setAdapter(adaptador);
//        lista_letras.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> padres, View elementos, int posision, long id) {
//                click_elemento_lista_letras(padres, elementos, posision, id);
//            }
//        });
//    }
//
//    private void click_elemento_lista_letras(AdapterView<?> parent, View view, int position, long id) {
//        //servicesList.setSelection(secciones_indice.get(lista_secciones.get(position)));
//        //        servicesList.smoothScrollToPositionFromTop(secciones_indice.get(lista_secciones.get(position)), 0, 500);
//        // servicesList.smoothScrollToPosition(secciones_indice.get(lista_secciones.get(position)));
//        Inicial inicial = iniciales.get(position);
//        int index = datos.indexOf(inicial.getLetra() + "#");
//        servicesList.smoothScrollToPositionFromTop(index, 0, 500);
//    }
//
//    private void evento() {
//        servicesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                click_elemento_lista_SI(parent, view, position, id);
//            }
//        });
//        text_buscar.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                evento_cambiar_texto_buscar(s);
//            }
//        });
//        text_buscar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                evento_selec_SI_bt_buscar(position);
//            }
//        });
//        bt_buscar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                click_bt_buscar();
//            }
//        });
//    }
//
//    private void click_bt_buscar() {
//        ConsumoPrincipal consumoPrincipal = (ConsumoPrincipal) context;
//        for (Servicio_Interno servicio_interno : datos) {
//            if (servicio_interno.getNombre().equals(text_buscar.getText().toString())) {
//                consumoPrincipal.ponerSIDialogInsertar(servicio_interno);
//                dismiss();
//                return;
//            }
//        }
//        dismiss();
//    }
//
//    private void click_elemento_lista_SI(AdapterView<?> parent, View view, int position, long id) {
//        if (!datos.get(position).getNombre().contains(MARCA_LETRA)) {
//            ConsumoPrincipal consumoPrincipal = (ConsumoPrincipal) context;
//            consumoPrincipal.ponerSIDialogInsertar(datos.get(position));
//            dismiss();
//        }
//    }
//
//    private void evento_selec_SI_bt_buscar(int pos) {
//        ListAdapter adaptador = text_buscar.getAdapter();
//        String nombre = (String) adaptador.getItem(pos);
//        int indice = datos.indexOf(nombre);
//        servicesList.smoothScrollToPositionFromTop(indice, 0, 500);
//    }
//
//    private void evento_cambiar_texto_buscar(Editable s) {
//        if (s.length() == 0) {
//            bt_buscar.setEnabled(false);
//        } else {
//            bt_buscar.setEnabled(true);
//        }
//    }
}
