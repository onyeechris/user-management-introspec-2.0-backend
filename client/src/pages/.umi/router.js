import React from 'react';
import { Router as DefaultRouter, Route, Switch } from 'react-router-dom';
import dynamic from 'umi/dynamic';
import renderRoutes from 'umi/_renderRoutes';


let Router = require('dva/router').routerRedux.ConnectedRouter;

let routes = [
  {
    "path": "/",
    "component": dynamic({ loader: () => import(/* webpackChunkName: "layouts__index" */'../../layouts/index.js'), loading: require('C:/Users/ACTIVEDGE-PC/Documents/user-management/user-management/src/components/Loader/Loader').default }),
    "routes": [
      {
        "path": "/404",
        "exact": true,
        "component": dynamic({ loader: () => import(/* webpackChunkName: "p__404" */'../404.js'), loading: require('C:/Users/ACTIVEDGE-PC/Documents/user-management/user-management/src/components/Loader/Loader').default })
      },
      {
        "path": "/:lang(en)/404",
        "exact": true,
        "component": dynamic({ loader: () => import(/* webpackChunkName: "p__404" */'../404.js'), loading: require('C:/Users/ACTIVEDGE-PC/Documents/user-management/user-management/src/components/Loader/Loader').default })
      },
      {
        "path": "/",
        "exact": true,
        "component": dynamic({ loader: () => import(/* webpackChunkName: "p__index" */'../index.js'), loading: require('C:/Users/ACTIVEDGE-PC/Documents/user-management/user-management/src/components/Loader/Loader').default })
      },
      {
        "path": "/:lang(en)/",
        "exact": true,
        "component": dynamic({ loader: () => import(/* webpackChunkName: "p__index" */'../index.js'), loading: require('C:/Users/ACTIVEDGE-PC/Documents/user-management/user-management/src/components/Loader/Loader').default })
      },
      {
        "path": "/chart/ECharts",
        "exact": true,
        "component": dynamic({ loader: () => import(/* webpackChunkName: "p__chart__ECharts__index" */'../chart/ECharts/index.js'), loading: require('C:/Users/ACTIVEDGE-PC/Documents/user-management/user-management/src/components/Loader/Loader').default })
      },
      {
        "path": "/:lang(en)/chart/ECharts",
        "exact": true,
        "component": dynamic({ loader: () => import(/* webpackChunkName: "p__chart__ECharts__index" */'../chart/ECharts/index.js'), loading: require('C:/Users/ACTIVEDGE-PC/Documents/user-management/user-management/src/components/Loader/Loader').default })
      },
      {
        "path": "/chart/highCharts",
        "exact": true,
        "component": dynamic({ loader: () => import(/* webpackChunkName: "p__chart__highCharts__index" */'../chart/highCharts/index.js'), loading: require('C:/Users/ACTIVEDGE-PC/Documents/user-management/user-management/src/components/Loader/Loader').default })
      },
      {
        "path": "/:lang(en)/chart/highCharts",
        "exact": true,
        "component": dynamic({ loader: () => import(/* webpackChunkName: "p__chart__highCharts__index" */'../chart/highCharts/index.js'), loading: require('C:/Users/ACTIVEDGE-PC/Documents/user-management/user-management/src/components/Loader/Loader').default })
      },
      {
        "path": "/chart/Recharts",
        "exact": true,
        "component": dynamic({ loader: () => import(/* webpackChunkName: "p__chart__Recharts__index" */'../chart/Recharts/index.js'), loading: require('C:/Users/ACTIVEDGE-PC/Documents/user-management/user-management/src/components/Loader/Loader').default })
      },
      {
        "path": "/:lang(en)/chart/Recharts",
        "exact": true,
        "component": dynamic({ loader: () => import(/* webpackChunkName: "p__chart__Recharts__index" */'../chart/Recharts/index.js'), loading: require('C:/Users/ACTIVEDGE-PC/Documents/user-management/user-management/src/components/Loader/Loader').default })
      },
      {
        "path": "/dashboard",
        "exact": true,
        "component": dynamic({ loader: () => import(/* webpackChunkName: "p__dashboard__index" */'../dashboard/index.js'), loading: require('C:/Users/ACTIVEDGE-PC/Documents/user-management/user-management/src/components/Loader/Loader').default })
      },
      {
        "path": "/:lang(en)/dashboard",
        "exact": true,
        "component": dynamic({ loader: () => import(/* webpackChunkName: "p__dashboard__index" */'../dashboard/index.js'), loading: require('C:/Users/ACTIVEDGE-PC/Documents/user-management/user-management/src/components/Loader/Loader').default })
      },
      {
        "path": "/login",
        "exact": true,
        "component": dynamic({ loader: () => import(/* webpackChunkName: "p__login__index" */'../login/index.js'), loading: require('C:/Users/ACTIVEDGE-PC/Documents/user-management/user-management/src/components/Loader/Loader').default })
      },
      {
        "path": "/:lang(en)/login",
        "exact": true,
        "component": dynamic({ loader: () => import(/* webpackChunkName: "p__login__index" */'../login/index.js'), loading: require('C:/Users/ACTIVEDGE-PC/Documents/user-management/user-management/src/components/Loader/Loader').default })
      },
      {
        "path": "/post",
        "exact": true,
        "component": dynamic({ loader: () => import(/* webpackChunkName: "p__post__index" */'../post/index.js'), loading: require('C:/Users/ACTIVEDGE-PC/Documents/user-management/user-management/src/components/Loader/Loader').default })
      },
      {
        "path": "/:lang(en)/post",
        "exact": true,
        "component": dynamic({ loader: () => import(/* webpackChunkName: "p__post__index" */'../post/index.js'), loading: require('C:/Users/ACTIVEDGE-PC/Documents/user-management/user-management/src/components/Loader/Loader').default })
      },
      {
        "path": "/request",
        "exact": true,
        "component": dynamic({ loader: () => import(/* webpackChunkName: "p__request__index" */'../request/index.js'), loading: require('C:/Users/ACTIVEDGE-PC/Documents/user-management/user-management/src/components/Loader/Loader').default })
      },
      {
        "path": "/:lang(en)/request",
        "exact": true,
        "component": dynamic({ loader: () => import(/* webpackChunkName: "p__request__index" */'../request/index.js'), loading: require('C:/Users/ACTIVEDGE-PC/Documents/user-management/user-management/src/components/Loader/Loader').default })
      },
      {
        "path": "/UIElement/editor",
        "exact": true,
        "component": dynamic({ loader: () => import(/* webpackChunkName: "p__UIElement__editor__index" */'../UIElement/editor/index.js'), loading: require('C:/Users/ACTIVEDGE-PC/Documents/user-management/user-management/src/components/Loader/Loader').default })
      },
      {
        "path": "/:lang(en)/UIElement/editor",
        "exact": true,
        "component": dynamic({ loader: () => import(/* webpackChunkName: "p__UIElement__editor__index" */'../UIElement/editor/index.js'), loading: require('C:/Users/ACTIVEDGE-PC/Documents/user-management/user-management/src/components/Loader/Loader').default })
      },
      {
        "path": "/user",
        "exact": true,
        "component": dynamic({ loader: () => import(/* webpackChunkName: "p__user__index" */'../user/index.js'), loading: require('C:/Users/ACTIVEDGE-PC/Documents/user-management/user-management/src/components/Loader/Loader').default })
      },
      {
        "path": "/:lang(en)/user",
        "exact": true,
        "component": dynamic({ loader: () => import(/* webpackChunkName: "p__user__index" */'../user/index.js'), loading: require('C:/Users/ACTIVEDGE-PC/Documents/user-management/user-management/src/components/Loader/Loader').default })
      },
      {
        "path": "/user/:id",
        "exact": true,
        "component": dynamic({ loader: () => import(/* webpackChunkName: "p__user__$id__index" */'../user/$id/index.js'), loading: require('C:/Users/ACTIVEDGE-PC/Documents/user-management/user-management/src/components/Loader/Loader').default })
      },
      {
        "path": "/:lang(en)/user/:id",
        "exact": true,
        "component": dynamic({ loader: () => import(/* webpackChunkName: "p__user__$id__index" */'../user/$id/index.js'), loading: require('C:/Users/ACTIVEDGE-PC/Documents/user-management/user-management/src/components/Loader/Loader').default })
      },
      {
        "component": () => React.createElement(require('C:/Users/ACTIVEDGE-PC/Documents/user-management/user-management/node_modules/umi-build-dev/lib/plugins/404/NotFound.js').default, { pagesPath: 'src/pages', hasRoutesInConfig: false })
      }
    ]
  },
  {
    "component": () => React.createElement(require('C:/Users/ACTIVEDGE-PC/Documents/user-management/user-management/node_modules/umi-build-dev/lib/plugins/404/NotFound.js').default, { pagesPath: 'src/pages', hasRoutesInConfig: false })
  }
];
window.g_plugins.applyForEach('patchRoutes', { initialValue: routes });

export default function() {
  return (
<Router history={window.g_history}>
      { renderRoutes(routes, {}) }
    </Router>
  );
}
