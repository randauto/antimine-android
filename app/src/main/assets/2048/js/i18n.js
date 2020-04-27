/**
 * Internationalization methods for '2048' game.
 * 
 * @author Igor A. <a4vi2r@gmail.com>
 */


/**
 * Overwrites o1's values with o2's and adds o2's if non existent in o1.
 */
function mergeObjects(o1, o2) {
	var r = {};
	for ( var attrname in o1) {
		r[attrname] = o1[attrname];
	}
	for ( var attrname in o2) {
		r[attrname] = o2[attrname];
	}
	return r;
}

/**
 * Retrieve GET parameter's value from query.
 */
function findGetParameter(name) {
	var r = null, tmp = [];
	var items = location.search.substr(1).split('&');
	for (var index = 0; index < items.length; index++) {
		tmp = items[index].split('=');
		if (tmp[0] === name) {
			r = decodeURIComponent(tmp[1]);
		}
	}
	return r;
}

// Usage: i18n.get('key')
var i18n = new Localizer(findGetParameter('lang'));
