import dva from 'dva';
import createLoading from 'dva-loading';

const runtimeDva = window.g_plugins.mergeConfig('dva');
let app = dva({
  history: window.g_history,
  
  ...(runtimeDva.config || {}),
});

window.g_app = app;
app.use(createLoading());
(runtimeDva.plugins || []).forEach(plugin => {
  app.use(plugin);
});
app.use(require('../../plugins/onError.js').default);
app.use(require('C:/Users/ACTIVEDGE-PC/Documents/user-management/user-management/node_modules/dva-immer/lib/index.js').default());
app.model({ namespace: 'app', ...(require('C:/Users/ACTIVEDGE-PC/Documents/user-management/user-management/src/models/app.js').default) });
app.model({ namespace: 'model', ...(require('C:/Users/ACTIVEDGE-PC/Documents/user-management/user-management/src/pages/dashboard/model.js').default) });
app.model({ namespace: 'model', ...(require('C:/Users/ACTIVEDGE-PC/Documents/user-management/user-management/src/pages/login/model.js').default) });
app.model({ namespace: 'model', ...(require('C:/Users/ACTIVEDGE-PC/Documents/user-management/user-management/src/pages/post/model.js').default) });
app.model({ namespace: 'model', ...(require('C:/Users/ACTIVEDGE-PC/Documents/user-management/user-management/src/pages/user/model.js').default) });
app.model({ namespace: 'detail', ...(require('C:/Users/ACTIVEDGE-PC/Documents/user-management/user-management/src/pages/user/$id/models/detail.js').default) });
