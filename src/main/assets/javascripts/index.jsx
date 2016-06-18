var TlvHexForm = React.createClass({
    getInitialState: function() {
        return {data: "00 01 00 0C 00 02 00 03 61 62 63 00 03 00 01 64"};
    },
    handleDataChange: function(e) {
        this.setState({data: e.target.value});
        TlvParsingResult.bind(this, this.data);
    },
    render: function() {
        return (
            <div className="tlvHexForm">
                <h4>
                    TLV(HEX)
                </h4>
                <textarea
                    value={this.state.data}
                    onChange={this.handleDataChange} >
                </textarea>
                <TlvParsingResult url="" data={this.state.data} />
            </div>
        );
    }
});

var TlvParsingResult = React.createClass({
    getInitialState: function() {
        return {data: ""};
    },
    loadCommentsFromServer: function() {
        if (this.state.data != this.props.data) {
            this.state.data = this.props.data;
            $.ajax({
                url: this.props.url,
                dataType: 'json',
                contentType: 'application/json',
                cache: true,
                method: "POST",
                data: JSON.stringify({data: this.state.data}),
                success: function(data) {
                    var stringifyData = JSON.stringify(data, null, '\t');
                    this.setState({tlvResponse: stringifyData.substring(1, stringifyData.length - 1)});
                }.bind(this),
                error: function(data) {
                    this.setState({tlvResponse: data.responseText});
                }.bind(this)
            });
        }

    },
    componentDidMount: function() {
        if (this.props.data) {
            setInterval(this.loadCommentsFromServer, 500);
        }
    },
    jsonMarkup: function() {
        var md = new Remarkable();
        var jsonMarkup = md.render(this.state.tlvResponse);
        return { __html: jsonMarkup };
    },
    render: function() {
        return (
            <span dangerouslySetInnerHTML={this.jsonMarkup()} />
        );
    }
});

var Content = React.createClass({
    render: function() {
        return (
            <div className="content">
                <h1>TLV Parser</h1>
                <TlvHexForm />
            </div>
        );
    }
});

ReactDOM.render(
    React.createElement(Content, null),
    document.getElementById('content')
);