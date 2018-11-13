import React, { Component } from 'react';
import logo from './logo.svg';
import './App.css';

class App extends Component {
    constructor(props) {
        super(props);
        this.state = {
            // items:[],
            login: {},
            isLoaded: false
        };
    }

    componentDidMount() {
        // fetch('https://jsonplaceholder.typicode.com/users')
        // // fetch('auth')
        //     .then(res => res.json())
        //     .then(json => {
        //         this.setState({
        //             isLoaded: true,
        //             items: json,
        //         })
        //     });
        fetch('auth', {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                username: 'admean',
                password: '56789',
            })
        })
        .then(res => res.json())
        .then(json => {
            this.setState({
                isLoaded: true,
                login: json,
            })
        })
        // .then(json => console.log(json))

    }

  render() {
        // var { isLoaded, items } = this.state;
        var { isLoaded, login } = this.state;

        if(!isLoaded) {
            return <div>Loading...</div>;
        } else {
            return(
                <div className="App">
                    <header className="App-header">
                        <img src={logo} className="App-logo" alt="logo" />
                        <p>
                        Edit <code>src/App.js</code> and save to reload.
                        </p>

                        <p>
                            Token: {login.token}
                        </p>

                        {/*<ul>*/}
                            {/*{items.map(item => (*/}
                                {/*<li key={item.id}>*/}
                                    {/*Name: {item.name} |  Email: {item.email}*/}
                                {/*</li>*/}
                            {/*))}*/}
                        {/*</ul>*/}

                    </header>
                </div>
            );
        }
  }
}

export default App;
