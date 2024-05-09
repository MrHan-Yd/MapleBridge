

function isEmpty(array) {
    return Array.isArray(array) && array.length === 0;
}

function stringTurnInt(str) {
    return Number.parseInt(str) ;
}

/* 等级徽章 */
function levelBadge(level) {
    if (level === '1' || level === '2') {
        return '/images/level/1、2.png';
    } else if (level === '3') {
        return '/images/level/3.png';
    } else if (level === '4') {
        return '/images/level/4.png';
    } else if (level === '5') {
        return '/images/level/5.png';
    } else if (level === '6') {
        return '/images/level/6.png';
    } else if (level === '7') {
        return '/images/level/7.png';
    }
}
export {
    isEmpty,
    stringTurnInt,
    levelBadge
}