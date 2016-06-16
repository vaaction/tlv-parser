var TlvHexForm = React.createClass({
    render: function() {
        return (
            <div className="tlvHexForm">
                <h3>
                    Throw your TLV(HEX) to the FIRE!!!
                </h3>
                <textarea>
                    23 FF CC A3
                </textarea>
            </div>
        );
    }
});

var TlvParsingResult = React.createClass({
    render: function() {
        return (
            <div className="tlvParsingResult">
                Your TLV is fucking bullshit
            </div>
        );
    }
});

var Content = React.createClass({
    render: function() {
        return (
            <div className="content">
                <h1>The Best TLV Parser</h1>
                <TlvHexForm />
                <TlvParsingResult />
            </div>
        );
    }
});

ReactDOM.render(
    React.createElement(Content, null),
    document.getElementById('content')
);