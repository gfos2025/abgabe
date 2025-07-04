'use client'

import Link from 'next/link'
import { usePathname } from 'next/navigation'
import { buttonVariants } from './ui/button'
import { cn } from '@/lib/utils'
import { ModeToggle } from './mode-toggle'
import { Menu } from 'lucide-react'
import { Search } from './Search'
import {
    Sheet,
    SheetContent,
    SheetTitle,
    SheetTrigger,
} from "@/components/ui/sheet"
import { MobileNav } from './MobileNav'
import { NavTreeItem, VersionInfo } from '@/lib/mkdocs'

interface NavbarProps {
    items: Array<{
        title: string
        path?: string
    }>
    fullNavigation: NavTreeItem[],
    versions?: VersionInfo[],
    currentVersion?: string
}

export const Navbar = ({ items, fullNavigation }: NavbarProps) => {
    const pathname = usePathname()

    return (
        <header className="sticky border-b-[1px] top-0 z-40 bg-background dark:bg-background py-2 px-2">
            <div className=''>
                <div className="flex justify-between align-center items-center ">
                    <Link href="/" className="ml-2 font-bold text-xl flex">
                        Worksale
                    </Link>


                    <div className="flex lg:hidden items-center">
                        <ModeToggle />
                        <Sheet>
                            <SheetTitle className='flex items-center h-full'><SheetTrigger className="px-2 lg:hidden h-full">
                                <Menu className="h-5 w-5" aria-label="Menu Icon" />
                            </SheetTrigger>
                            </SheetTitle>
                            <SheetContent side="left" className="w-[300px] sm:w-[400px] overflow-y-auto">
                                <MobileNav items={items} fullNavigation={fullNavigation} />
                            </SheetContent>
                        </Sheet>
                    </div>

                    <nav className="hidden lg:flex gap-2">
                        {items.map((item) => (
                            <Link
                                key={item.title}
                                href={item.path ? `/${item.path}` : '#'}
                                className={cn(
                                    buttonVariants({ variant: "ghost" }),
                                    pathname === `/${item.path}` && "bg-muted"
                                )}
                            >
                                {item.title}
                            </Link>
                        ))}
                    </nav>

                    <div className="hidden lg:flex gap-2">
                        <ModeToggle />
                    </div>
                </div>
            </div>
        </header>
    )
}
