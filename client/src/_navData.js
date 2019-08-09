import React, { Component } from 'react';

import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import { fetchTodos } from './actions/action_todo';

class TodoData extends Component {

  constructor(props) {
    super(props);
    this.state = {
      todoData: null
    }
  }

  componentDidMount() {
    // console.log(this.props);
    this.props.fetchTodos().then(result => {
      this.setState({ todoData: result.data.payload.length });
      // console.log(this.state.todoData);
    })
    // console.log(this.state.todoData);
  }

  render() {
    this.props.fetchTodos();
    return (
      <div>
        {this.props.todoData.length}
      </div>
    );
  }
}

const mapStateToProps = (state) => {
  // console.log('State is ', state)
  return {
    todoData: state.todo.todosFetched ? state.todo.todosFetched.data.payload : '',
  }
}

const mapDispatchToProps = (dispatch) => {
  return bindActionCreators({
    fetchTodos
  }, dispatch)
}

export default connect(mapStateToProps, mapDispatchToProps)(TodoData);
// export default TodoData;