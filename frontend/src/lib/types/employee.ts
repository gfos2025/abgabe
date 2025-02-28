import { z } from "zod";

export const employeeSchema = z.object({
    id: z.string().uuid(),
    name: z.string().default("no-name"),
    surname: z.string().default("no-surname"),
    email: z.string().default("no-email"),
    role: z.string().default("no-role"),

}).optional();


export type Employee = z.infer<typeof employeeSchema>;