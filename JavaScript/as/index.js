var data = {
	operands: [],
	operators: [],
	lastElement: 0
}

var btns = document.getElementsByClassName( "btn" );
var consolePanel = document.getElementById( "console" );
var consolePanelFoot = document.getElementById( "consoleFoot" );

for ( var i = 0; i < btns.length; i++ ) {
	btns[ i ].addEventListener( "click", ( event ) => {
		var valid = getParsedInput( event.srcElement.innerText );

		if ( valid !== undefined ) {

			if ( valid === event.srcElement.innerText ) consolePanel.innerText += valid;
			else {
				consolePanelFoot.innerText = consolePanel.innerText;
				consolePanel.innerText = valid;

			}
		}
		console.log( data.operands, data.operators, data.lastElement );
	}, false )
}

var getParsedInput = ( input ) => {
	var match = input.match( /[0-9A-F]/ );
	if ( match && match[ 0 ] === match[ 'input' ] )
		return handleOperands( input );
	return handleOperation( input );

};

var handleOperands = ( num ) => {
	if ( ( data.operands.length === 1 && data.operators.length >= 1 ) || ( data.operands.length === 0 ) ) {
		if ( data.lastElement === 0 && data.operands.length !== 0 ) {
			data.operands.push( data.operands.pop() + num );
		} else data.operands.push( num );
		data.lastElement = 0;
		return num;
	} else if ( data.operands.length === 1 || data.operands.length === 2 ) {
		data.operands.push( data.operands.pop() + num );
		data.lastElement = 0;
		return num;
	}
}

var handleOperation = ( input ) => {
	if ( input === 'CLR' ) {
		data.operands = [];
		data.operators = [];
		consolePanel.innerText = "";
		consolePanelFoot.innerText = "";
		return;
	}
	if ( input === '←' ) {
		if ( data.lastElement === 0 ) data.operands.pop();
		else data.operators.pop();
		return consolePanel.innerText.slice( 0, consolePanel.innerText.length - 2 )
	}
	if ( data.operators.length === 1 && data.operands.length === 2 ) {
		var op2 = parseInt( data.operands.pop(), 16 );
		var op1 = parseInt( data.operands.pop(), 16 );
		var op = data.operators.pop();
		var res = performOperaion( op1, op, op2 );

		if ( input !== '=' ) data.operators.push( input );
		data.operands.push( res );
		data.lastElement = 1;
		return ( input === '=' ) ? res + " " : `${res} ${input} `;
	} else if ( data.operators.length === 0 && data.operands.length === 1 ) {
		data.operators.push( input );
		data.lastElement = 1;
		return input;
	} else if ( data.operators.length === 0 && input.match( /[+-~]/ ) ) {
		data.operators.push( input );
		data.lastElement = 1;
		return input;
	} else if ( data.operators.length === 1 && data.operands.length === 1 ) {
		if ( data.lastElement === 0 ) {
			var op1 = parseInt( data.operands[ 0 ], 16 );
			var op = data.operators[ 0 ];
			if ( op.match( /[+-~]/ ) ) {
				var res = performOperaion( op1, op );
				data.operands.pop();
				data.operators.pop();
				if ( input !== '=' ) data.operators.push( input );
				data.operands.push( res );
				data.lastElement = 1;
				return ( input === '=' ) ? res + " " : `${res} ${input} `;
			}
		} else {
			data.operators.push( input );
			data.lastElement = 1;
			return input;
		}
	} else if ( data.operators.length === 2 && data.operands.length === 2 ) {
		var op22 = parseInt( performOperaion( parseInt( data.operands.pop(), 16 ), data.operators.pop() ), 16 );
		var op1 = data.operands.pop();
		var op = data.operators.pop();
		var res = performOperaion( parseInt( op1, 16 ), op, op22 );

		if ( input !== '=' ) data.operators.push( input );
		data.lastElement = 1;
		data.operands.push( res );
		return ( input === '=' ) ? res + " " : `${res} ${input} `;
	}
}

var performOperaion = ( op1, op, op2 ) => {
	console.log( op1 + op + op2 )
	switch ( op ) {
		case '+':
			if ( op2 !== undefined ) {
				return ( op1 + op2 ).toString( 16 ).toUpperCase();
			} else return op1.toString( 16 );
			break;
		case '-':
			if ( op2 !== undefined ) {
				return ( op1 - op2 ).toString( 16 ).toUpperCase();
			} else return ( -op1 ).toString( 16 );
			break;
		case '*':
			return ( op1 * op2 ).toString( 16 ).toUpperCase();
			break;
		case '/':
			return ( op1 / op2 ).toString( 16 ).toUpperCase();
			break;
		case '%':
			return ( op1 % op2 ).toString( 16 ).toUpperCase();
			break;
		case '~':
			return ( ~op1 ).toString( 16 ).toUpperCase();
			break;
		case '&':
			return ( op1 & op2 ).toString( 16 ).toUpperCase();
			break;
		case '|':
			return ( op1 | op2 ).toString( 16 ).toUpperCase();
			break;
	}
}