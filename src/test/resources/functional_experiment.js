/**
 * Prepends head to tail.
 * 
 * @param {type} head an arbitrary object or primitive value.
 * @param {type} tail a linked list or null.
 * @returns 
 */
function cons(head, tail) {
    return function(fn) {
        return fn(head, tail);
    };
}

/*
 * Helper function that recursively pops array elements into a linked list.
 * 
 * @param {type} list to prepend the values to or null for the empty list.
 * @param {type} values array of arbitrary elements
 * @returns a linked list of the arguments. 
 */
function buildList(list, values) {
    if (values.length === 0) {
        return list;
    }  
    var nextVal = values.pop();
    return buildList(cons(nextVal, list), values);
}

/**
 * Converts all arguments into a functional structure that behaves like a linked list.
 * 
 * @returns a linked list of the arguments.
 */
function asList() {
    var args = Array.prototype.slice.call(arguments);
    return buildList(null, args);
}

function isEmpty(list) {
    return list === null;
}

/**
 * Returns the first element of a linked list.
 * 
 * @param {type} list a linked list. Must not be null.
 * 
 * @returns {unresolved}
 */
function head(list) {
    return list(function(head, tail) {
        return head;
    });
}

/**
 * Returns all but the first element of a linked list.
 * 
 * @param {type} list a linked list. Must not be null.
 * 
 * @returns
 */
function tail(list) {
    return list(function(head, tail) {
        return tail;
    });
}

function map(fn, list) {
    if (isEmpty(list)) {
        return null;
    }
    return cons(fn(head(list)), map(fn, tail(list)));
}

function forEach(list, fn) {
    if (!isEmpty(list)) {
        fn(head(list));
        forEach(tail(list), fn);
    }
}

function filter(pred, list) {
    if (isEmpty(list)) {
        return list;
    }
    var first = head(list);
    var rest = tail(list);
    if (pred(first)) {
        return cons(first, filter(pred, rest));
    }
    return filter(pred, rest);
}

function foldLeft(fn, initial, list) {
    if (isEmpty(list)) {
        return initial;
    }
    return foldLeft(fn, fn(initial, head(list)), tail(list));
}

function invert(list) {
    return foldLeft(function(acc, elem) {return cons(elem, acc);}, null, list);
} 

function size(list) {
    return foldLeft(function(acc) {return acc + 1;}, 0, list);
}

function log(list) {
    forEach(list, function(elem) {console.log(elem);});
}