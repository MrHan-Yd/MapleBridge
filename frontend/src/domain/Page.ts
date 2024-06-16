class Page {
    private _total?: number = 0
    private _size?: number = 10
    private _current?: number = 1
    private _pages?: number | undefined = 0

    get total(): number {
        return this._total as number
    }

    set total(total: number) {
        this._total = total
    }

    get size(): number {
        return this._size as number
    }

    set size(size: number) {
        this._size = size
    }

    get current(): number {
        return this._current as number
    }

    set current(current: number) {
        this._current = current
    }

    get pages(): number | undefined {
        return this._pages as number
    }

    set pages(pages: number) {
        this._pages = pages
    }
}

export default Page